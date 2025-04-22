FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/localconnect-api-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (or any port your application uses)
EXPOSE 8080

# Run the JAR file using java
CMD ["java", "-jar", "app.jar"]
