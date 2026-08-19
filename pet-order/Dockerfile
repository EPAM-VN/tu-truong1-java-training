# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder
LABEL authors="TuTruong"

WORKDIR /app

# Copy parent pom and module poms first for dependency caching
COPY pom.xml .

COPY order.core/pom.xml order.core/
COPY order.infrastructure/pom.xml order.infrastructure/
COPY order.application/pom.xml order.application/
COPY order.web/pom.xml order.web/

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY . .

# Build only web and required modules
RUN mvn clean package -pl order.web -am -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/order.web/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]