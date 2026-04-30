# =========================
# Stage 1: Build with Maven
# =========================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and modules
COPY pom.xml .
COPY monitoring-agent/pom.xml monitoring-agent/
COPY monitoring-server/pom.xml monitoring-server/
COPY monitoring-commonn/pom.xml monitoring-commonn/

# Download dependencies (layer caching)
RUN mvn dependency:go-offline -B

# Copy full source
COPY . .

# Build the project (skip tests for faster build)
RUN mvn clean package -DskipTests

# =========================
# Stage 2: Runtime Image
# =========================
FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/monitoring-server/target/monitoring-server-*.jar app.jar

# Expose port (adjust if your Spring Boot app runs on a different port)
EXPOSE 9090

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
