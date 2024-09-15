# Stage 1: Build Stage
FROM maven:3.8.3-openjdk-17 AS builder
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Stage 2: Final Stage
FROM openjdk:17-jdk
WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/target/raven-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

