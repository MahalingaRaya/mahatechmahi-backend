# Build stage
FROM maven:3.8.5-openjdk-24 AS build
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:24-jdk-slim
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]