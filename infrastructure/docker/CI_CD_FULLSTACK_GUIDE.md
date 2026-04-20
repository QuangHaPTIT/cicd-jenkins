# 🚀 CẨM NANG CI/CD FULL-STACK TỪ LOCAL LÊN AWS

Kiến trúc: **Nuxt.js (FE) + Spring Boot (BE) + PostgreSQL + Redis + Nginx + Jenkins + Docker**.

> Phiên bản: dùng Nginx làm Reverse Proxy, tách biệt Frontend/Backend.

---

## GIAI ĐOẠN 1: CHUẨN BỊ MÃ NGUỒN (Local)

### 1) Bản vẽ hạ tầng `docker-compose.yml` (trên App Server)

Mục tiêu: giấu hoàn toàn cổng FE (3000) và BE (8080), chỉ public cổng 80 qua Nginx.

```yaml
version: '3.8'
services:
  nginx:
    image: nginx:alpine
    container_name: gateway-nginx
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
    depends_on:
      - frontend-app
      - backend-app
    networks:
      - app-network

  frontend-app:
    image: <USER_DOCKERHUB>/qlncc-fe:latest
    container_name: my-nuxt-app
    networks:
      - app-network

  backend-app:
    image: <USER_DOCKERHUB>/qlncc-be:latest
    container_name: my-spring-app
    depends_on:
      - postgres
      - redis
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/mydb
      SPRING_DATASOURCE_USERNAME: myuser
      SPRING_DATASOURCE_PASSWORD: mypassword
      SPRING_DATA_REDIS_HOST: redis
      SPRING_DATA_REDIS_PORT: 6379
    networks:
      - app-network

  postgres:
    image: postgres:15-alpine
    container_name: my-postgres
    environment:
      POSTGRES_USER: myuser
      POSTGRES_PASSWORD: mypassword
      POSTGRES_DB: mydb
    volumes:
      - postgres-data:/var/lib/postgresql/data
    networks:
      - app-network

  redis:
    image: redis:7-alpine
    container_name: my-redis
    networks:
      - app-network

volumes:
  postgres-data:
networks:
  app-network:
    driver: bridge
```

### 2) Cấu hình phân luồng `nginx.conf`

```nginx
events {}
http {
    server {
        listen 80;

        # Khách gọi API -> Đẩy vào Spring Boot
        location /api/ {
            proxy_pass http://backend-app:8080/;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
        }

        # Khách truy cập web -> Đẩy vào frontend container
        location / {
            proxy_pass http://frontend-app:80/;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
        }
    }
}
```

### 3) Dockerfile cho FE và BE

- **Backend (Java)**: dùng multi-stage `eclipse-temurin:21` (giữ như hiện tại).
- **Frontend (Nuxt.js)**:

```dockerfile
FROM node:18-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
EXPOSE 3000
ENV HOST=0.0.0.0
CMD ["npm", "run", "start"]
```

### 4) Kịch bản CI/CD (`Jenkinsfile`)

> Dùng cho cả 2 repo FE/BE, chỉ đổi `IMAGE_NAME` và service deploy.

```groovy
pipeline {
    agent any
    environment {
        DOCKER_HUB_USER = '<USER_DOCKERHUB>'
        APP_SERVER_IP = '<IP_CON_EC2_APP>'
        IMAGE_NAME = 'qlncc-be' // Đổi thành qlncc-fe nếu ở repo Frontend
    }
    triggers { githubPush() }
    stages {
        stage('1. Checkout Code') {
            steps { checkout scm }
        }
        stage('2. Build & Push Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_HUB_USER}/${IMAGE_NAME}:latest ."
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                    sh "echo $PASS | docker login -u $USER --password-stdin"
                    sh "docker push ${DOCKER_HUB_USER}/${IMAGE_NAME}:latest"
                }
            }
        }
        stage('3. Deploy (Chỉ cập nhật Container tương ứng)') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ec2-ssh-key', keyFileVariable: 'KEY')]) {
                    sh """
                    ssh -i ${KEY} -o StrictHostKeyChecking=no ubuntu@${APP_SERVER_IP} << 'EOF'
                        cd /home/ubuntu/
                        docker pull ${DOCKER_HUB_USER}/${IMAGE_NAME}:latest
                        docker-compose up -d --no-deps --build backend-app
                        # Nếu là repo FE -> đổi thành frontend-app
                    EOF
                    """
                }
            }
        }
    }
    post { always { sh "docker system prune -f" } }
}
```

---

## GIAI ĐOẠN 2: THIẾT LẬP AWS & BẢO MẬT

1. Tạo S3 Bucket (tùy chọn cho BE, lưu file backup).
2. Tạo IAM Role cho EC2 Jenkins (gắn `AmazonS3FullAccess` nếu cần).
3. Security Group:
   - Jenkins EC2: mở **8080** (Jenkins + GitHub webhook).
   - App EC2: mở **80, 443, 22**.
   - Đóng public các port **8080, 3000, 5432, 6379**.

---

## GIAI ĐOẠN 3: CÀI ĐẶT MÁY CHỦ

### 1) Jenkins Server (EC2 1)

```bash
sudo apt update -y && sudo apt install docker.io unzip -y
sudo usermod -aG docker jenkins && sudo usermod -aG docker ubuntu
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip && sudo ./aws/install
sudo systemctl restart jenkins && sudo reboot
```

### 2) Application Server (EC2 2)

```bash
sudo apt update -y && sudo apt install docker.io docker-compose -y
sudo usermod -aG docker ubuntu
sudo reboot
```

Sau reboot, copy sẵn `docker-compose.yml` và `nginx.conf` vào `/home/ubuntu/`.

---

## GIAI ĐOẠN 4: KẾT NỐI TRÊN JENKINS

1. **Credentials**:
   - `docker-hub-creds` (Username/Password)
   - `ec2-ssh-key` (SSH private key từ `.pem` của App EC2)
2. **Tạo 2 Pipeline jobs**:
   - `qlncc-backend` -> repo Spring Boot
   - `qlncc-frontend` -> repo Nuxt.js
3. **Webhook GitHub** cho cả 2 repo:
   - Payload URL: `http://<IP_JENKINS>:8080/github-webhook/`
   - Content type: `application/json`

---

## 🎉 Kết quả vận hành

- FE push code -> cập nhật giao diện qua Nginx.
- BE push code -> cập nhật API, DB/Redis giữ dữ liệu.
- Toàn bộ traffic đi qua Nginx gateway.

---

## Ghi chú áp dụng cho repo hiện tại (QLNCC)

- Repo hiện tại FE là **Vue 3 + Vite** (không phải Nuxt).
- Nếu dùng đúng stack hiện tại, cần đổi:
  - Dockerfile FE phù hợp Vite build/serve.
  - Nginx `proxy_pass` cho frontend theo cổng container FE thực tế (ví dụ FE serve qua Nginx thì dùng `frontend-app:80`).
