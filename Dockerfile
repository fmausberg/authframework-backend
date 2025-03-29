FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/authframework-backend.jar authframework-backend.jar
ENTRYPOINT ["java", "-jar", "authframework-backend.jar"]