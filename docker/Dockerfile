FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src
COPY pipe-springboot/pom.xml ./
COPY pipe-springboot/src/ ./src/
COPY docker/application.yml ./src/main/resources/application.yml
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /src/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
