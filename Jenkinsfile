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
else
    echo "docker compose v2 plugin not found, installing standalone plugin"
    COMPOSE_VERSION="v2.29.7"
    COMPOSE_DIR="$HOME/.docker/cli-plugins"
    COMPOSE_BIN="$COMPOSE_DIR/docker-compose"
    mkdir -p "$COMPOSE_DIR"

    ARCH="$(uname -m)"
    case "$ARCH" in
        x86_64|amd64) ARCH="x86_64" ;;
        aarch64|arm64) ARCH="aarch64" ;;
        *)
            echo "Unsupported architecture for docker compose: $ARCH"
            exit 125
            ;;
    esac

    URL="https://github.com/docker/compose/releases/download/$COMPOSE_VERSION/docker-compose-linux-$ARCH"
    if command -v curl >/dev/null 2>&1; then
        curl -fL "$URL" -o "$COMPOSE_BIN"
    elif command -v wget >/dev/null 2>&1; then
        wget -O "$COMPOSE_BIN" "$URL"
    else
        echo "Neither curl nor wget is available on remote host"
        exit 125
    fi
    chmod +x "$COMPOSE_BIN"
fi
docker compose version
docker compose -f docker-compose.yml up -d --no-deps --force-recreate frontend-app
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
else
    echo "docker compose v2 plugin not found, installing standalone plugin"
    COMPOSE_VERSION="v2.29.7"
    COMPOSE_DIR="$HOME/.docker/cli-plugins"
    COMPOSE_BIN="$COMPOSE_DIR/docker-compose"
    mkdir -p "$COMPOSE_DIR"

    ARCH="$(uname -m)"
    case "$ARCH" in
        x86_64|amd64) ARCH="x86_64" ;;
        aarch64|arm64) ARCH="aarch64" ;;
        *)
            echo "Unsupported architecture for docker compose: $ARCH"
            exit 125
            ;;
    esac

    URL="https://github.com/docker/compose/releases/download/$COMPOSE_VERSION/docker-compose-linux-$ARCH"
    if command -v curl >/dev/null 2>&1; then
        curl -fL "$URL" -o "$COMPOSE_BIN"
    elif command -v wget >/dev/null 2>&1; then
        wget -O "$COMPOSE_BIN" "$URL"
    else
        echo "Neither curl nor wget is available on remote host"
        exit 125
    fi
    chmod +x "$COMPOSE_BIN"
fi
docker compose version
docker compose -f docker-compose.yml up -d --no-deps --force-recreate backend-app
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
