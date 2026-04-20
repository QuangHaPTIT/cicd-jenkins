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
                    sh """
scp -i ${KEY} -o StrictHostKeyChecking=no infrastructure/docker/docker-compose.yml ubuntu@${APP_SERVER_IP}:/home/ubuntu/
scp -i ${KEY} -o StrictHostKeyChecking=no infrastructure/nginx/nginx.conf ubuntu@${APP_SERVER_IP}:/home/ubuntu/

ssh -i ${KEY} -o StrictHostKeyChecking=no ubuntu@${APP_SERVER_IP} << 'EOF'
cd /home/ubuntu/
export DOCKER_HUB_USER=${DOCKER_HUB_USER}
docker pull ${DOCKER_HUB_USER}/qlncc-fe:latest
docker-compose up -d --no-deps --build frontend-app
EOF
                    """
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
                    sh """
scp -i ${KEY} -o StrictHostKeyChecking=no infrastructure/docker/docker-compose.yml ubuntu@${APP_SERVER_IP}:/home/ubuntu/
scp -i ${KEY} -o StrictHostKeyChecking=no infrastructure/nginx/nginx.conf ubuntu@${APP_SERVER_IP}:/home/ubuntu/

ssh -i ${KEY} -o StrictHostKeyChecking=no ubuntu@${APP_SERVER_IP} << 'EOF'
cd /home/ubuntu/
export DOCKER_HUB_USER=${DOCKER_HUB_USER}
docker pull ${DOCKER_HUB_USER}/qlncc-be:latest
docker-compose up -d --no-deps --build backend-app
EOF
                    """
                }
            }
        }
    }

    post {
        always { sh "docker system prune -f" }
    }
}
