# Multi-stage Dockerfile for building and running the Spring Boot application
# Stage 1: build the application using Maven (uses official Maven image with JDK 17)
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy only Maven files first to leverage Docker layer caching for deps
# Copy Maven POM only first to leverage Docker layer caching for dependencies
# (don't copy a non-existent .mvn directory; it's optional)
COPY pom.xml .

# Download dependencies
RUN mvn -B -DskipTests dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn -B -DskipTests package

# Stage 2: create a lightweight runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the fat jar from the build stage
COPY --from=build /workspace/target/*.jar ./app.jar

# Expose the port the app will listen on (Render sets $PORT at runtime)
EXPOSE 8080

# Allow passing additional JVM options via JAVA_OPTS
ENV JAVA_OPTS=""

# Start the app, binding to the PORT environment variable Render provides
# Use sh -c to allow variable expansion for $PORT
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
