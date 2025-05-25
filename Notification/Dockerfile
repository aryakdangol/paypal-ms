FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/Notification-1.0.0.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
