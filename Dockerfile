# Stage 1: Build the application
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies
# This is a good practice to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built JAR file from the build stage
# The JAR name is based on the artifactId and version in pom.xml
COPY --from=build /app/target/portfolio-api-*.jar app.jar

# Expose the application port (defaults to 8080 as per application.properties)
EXPOSE 8080

# Environment variables for the application (optional, can be overridden at runtime)
# ENV SPRING_DATASOURCE_URL=
# ENV SPRING_DATASOURCE_USERNAME=
# ENV SPRING_DATASOURCE_PASSWORD=
# ENV SPRING_MAIL_USERNAME=
# ENV SPRING_MAIL_PASSWORD=
# ENV CORS_ALLOWED_ORIGINS=

# Run the application
# We use the PORT environment variable if provided, otherwise default to 8080
# Added JVM flags for memory optimization on restricted environments like Render
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-XX:MinRAMPercentage=50.0", "-Xss256k", "-Dserver.port=${PORT:8080}", "-jar", "app.jar"]
