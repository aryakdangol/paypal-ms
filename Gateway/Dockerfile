FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/Gateway-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
