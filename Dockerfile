FROM maven:3.9-eclipse-temurin-21

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "target/comunidadvinos-0.0.1-SNAPSHOT.jar"]