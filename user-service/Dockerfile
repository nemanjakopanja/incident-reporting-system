# 1. faza - Build aplikacije
FROM maven:3.9.3-eclipse-temurin-17 as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. faza - Pokretanje aplikacije
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

ARG DEFAULT_PORT=8082
ENV PORT=$DEFAULT_PORT
EXPOSE $PORT

# Kopiranje JAR-a iz builder stage-a
COPY --from=builder /app/target/user-service-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
