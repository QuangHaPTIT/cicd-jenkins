# Stage 1: Build source code
FROM node:18-alpine AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Serve tĩnh bằng Nginx nhẹ
FROM nginx:alpine AS prod-stage
# Vite mặc định build ra thư mục 'dist'
COPY --from=build-stage /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
