// Monorepo CI/CD pipeline cho QLNCC (Frontend + Backend)
pipeline {
    agent any
    environment {
        DOCKER_HUB_USER = 'quangha22'
        APP_SERVER_IP   = '54.179.173.3'
    }

    triggers { githubPush() }

    stages {
        stage('Frontend: Build & Push') {
            when { changeset "QLNCC_client/**" }
            steps {
                echo "Phat hien thay doi Frontend. Dang build qlncc-fe..."
                sh "docker build -t ${DOCKER_HUB_USER}/qlncc-fe:latest -f QLNCC_client/Dockerfile QLNCC_client/"
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                    sh 'printf "%s" "$PASS" | tr -d "\r\n" | docker login -u "$USER" --password-stdin'
                    sh "docker push ${DOCKER_HUB_USER}/qlncc-fe:latest"
                }
            }
        }

        stage('Frontend: Deploy') {
            when { changeset "QLNCC_client/**" }
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ec2-ssh-key', keyFileVariable: 'KEY')]) {
                    sh '''
set -e
scp -i "$KEY" -o StrictHostKeyChecking=no infrastructure/docker/docker-compose.yml ubuntu@"$APP_SERVER_IP":/home/ubuntu/
scp -i "$KEY" -o StrictHostKeyChecking=no infrastructure/nginx/nginx.conf ubuntu@"$APP_SERVER_IP":/home/ubuntu/

ssh -T -i "$KEY" -o StrictHostKeyChecking=no ubuntu@"$APP_SERVER_IP" << EOF
set -e
cd /home/ubuntu/
export DOCKER_HUB_USER="$DOCKER_HUB_USER"
echo "Remote host:"
hostname
echo "Remote IPv4:"
hostname -I | cut -d' ' -f1
docker --version || true
docker pull "$DOCKER_HUB_USER/qlncc-fe:latest"

if docker compose version >/dev/null 2>&1; then
    docker compose version
    docker compose -f docker-compose.yml up -d --no-deps frontend-app
else
    echo "docker compose v2 plugin not found, using docker/compose:2.29.7"
    docker run --rm \
      -e DOCKER_HUB_USER="$DOCKER_HUB_USER" \
      -v /var/run/docker.sock:/var/run/docker.sock \
      -v "$PWD:$PWD" \
      -w "$PWD" \
      docker/compose:2.29.7 \
      -f docker-compose.yml up -d --no-deps frontend-app
fi
EOF
                    '''
                }
            }
        }

        stage('Backend: Build & Push') {
            when { changeset "QLNCC_server/**" }
            steps {
                echo "Phat hien thay doi Backend. Dang build qlncc-be..."
                sh "docker build -t ${DOCKER_HUB_USER}/qlncc-be:latest -f QLNCC_server/Dockerfile QLNCC_server/"
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                    sh 'printf "%s" "$PASS" | tr -d "\r\n" | docker login -u "$USER" --password-stdin'
                    sh "docker push ${DOCKER_HUB_USER}/qlncc-be:latest"
                }
            }
        }

        stage('Backend: Deploy') {
            when { changeset "QLNCC_server/**" }
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ec2-ssh-key', keyFileVariable: 'KEY')]) {
                    sh '''
set -e
scp -i "$KEY" -o StrictHostKeyChecking=no infrastructure/docker/docker-compose.yml ubuntu@"$APP_SERVER_IP":/home/ubuntu/
scp -i "$KEY" -o StrictHostKeyChecking=no infrastructure/nginx/nginx.conf ubuntu@"$APP_SERVER_IP":/home/ubuntu/

ssh -T -i "$KEY" -o StrictHostKeyChecking=no ubuntu@"$APP_SERVER_IP" << EOF
set -e
cd /home/ubuntu/
export DOCKER_HUB_USER="$DOCKER_HUB_USER"
echo "Remote host:"
hostname
echo "Remote IPv4:"
hostname -I | cut -d' ' -f1
docker --version || true
docker pull "$DOCKER_HUB_USER/qlncc-be:latest"

if docker compose version >/dev/null 2>&1; then
    docker compose version
    docker compose -f docker-compose.yml up -d --no-deps backend-app
else
    echo "docker compose v2 plugin not found, using docker/compose:2.29.7"
    docker run --rm \
      -e DOCKER_HUB_USER="$DOCKER_HUB_USER" \
      -v /var/run/docker.sock:/var/run/docker.sock \
      -v "$PWD:$PWD" \
      -w "$PWD" \
      docker/compose:2.29.7 \
      -f docker-compose.yml up -d --no-deps backend-app
fi
EOF
                    '''
                }
            }
        }
    }

    post {
                always {
                        sh '''
if docker info >/dev/null 2>&1; then
    docker system prune -f
else
    echo "Skip docker system prune: Docker daemon unavailable"
fi
'''
                }
    }
}
