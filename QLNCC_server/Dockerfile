# cache stage
FROM eclipse-temurin:21-jdk AS cache-stage
WORKDIR /app
COPY gradlew ./
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon -Dorg.gradle.jvmargs="-Xmx512m"

# build stage
FROM eclipse-temurin:21-jdk AS build-stage
WORKDIR /app
COPY --from=cache-stage /root/.gradle /root/.gradle
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean bootJar -x test --no-daemon -Dorg.gradle.jvmargs="-Xmx512m"

# production stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build-stage /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Xmx400m","-jar","app.jar"]