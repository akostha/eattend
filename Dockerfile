# Use a base image with Java (e.g., OpenJDK)
FROM openjdk:17-jdk-alpine

# Set environment variables for database connection
ENV ETSERVICE_DB_URL=jdbc:mysql://ajaykdbserver.c7s8seeiu4fl.us-east-1.rds.amazonaws.com:3306/devldb
ENV ETSERVICE_DB_USERNAME=admin
ENV ETSERVICE_DB_PASSWORD=BARB0INDRAP

# Copy your Spring Boot JAR file into the image
COPY target/eattend-0.0.1.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app.jar"]
