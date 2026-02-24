# Build stage: compile Spring Boot app with Maven
# Build context: repo root; Dockerfile in backend/ so paths are backend/...
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy backend pom and source (when built from repo root: backend/...)
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B

COPY backend/src ./src
RUN mvn package -DskipTests -B

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Non-root user
RUN adduser -D appuser
USER appuser

COPY --from=build /app/target/*.jar app.jar

# Render sets PORT
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT} app.jar"]
