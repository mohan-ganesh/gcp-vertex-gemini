
FROM maven:3.6-jdk-17 as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn clean package -DskipTests


FROM adoptopenjdk/openjdk17:alpine-slim

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/vertex-gemini-0.0.1-snapshot*.jar /vertex-gemini-0.0.1-snapshot.jar

# Run the web service on container startup.
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=dev", "-jar", "/vertex-gemini-0.0.1-snapshot.jar"]