# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

COPY target/authframework-backend.jar authframework-backend.jar

# Make port 8080 available to the world outside the container
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "authframework-backend.jar"]
