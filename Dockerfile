FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy project files
COPY . .

# Build the project
RUN mvn -B -DskipTests clean package


# ---- Runtime stage ----
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy built JAR from previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java","-jar","/app/app.jar"]