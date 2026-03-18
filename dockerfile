# Build stage: create the Spring Boot fat jar without downloading Gradle at build time
FROM gradle:8.14.3-jdk17 AS build
WORKDIR /workspace
COPY . .
RUN gradle --no-daemon clean bootJar

# Runtime stage: run the jar with access to the SQLite file
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/build/libs/emoblog-backend-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p db

# Persist the SQLite database under /app/db to match application.properties
VOLUME ["/app/db"]

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
