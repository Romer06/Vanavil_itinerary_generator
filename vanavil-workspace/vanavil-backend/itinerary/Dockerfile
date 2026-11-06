# Use an official lightweight Java runtime
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy maven wrapper and pom files first to cache dependencies
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy the rest of your source code and build the jar
COPY src ./src
RUN ./mvnw package -DskipTests

# Find the built jar and run it
RUN cp target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]