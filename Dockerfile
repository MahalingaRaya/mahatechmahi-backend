# Build stage
FROM eclipse-temurin:24-jdk AS build
WORKDIR /app
COPY . .
# Make the Maven wrapper executable
RUN chmod +x mvnw
# Build the project using the wrapper
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:24-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]