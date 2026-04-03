FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/target/portfolio-api-*.jar app.jar

ENV PORT=8080
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=validate

EXPOSE 8080

# Use shell form so PORT can be expanded at container runtime.
ENTRYPOINT ["sh", "-c", "java -XX:MaxRAMPercentage=75.0 -XX:MinRAMPercentage=50.0 -Xss256k -Dserver.port=${PORT} -jar app.jar"]
