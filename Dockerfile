FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /usr/src/app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /usr/src/app/target/quarkus-app/lib/ /app/lib/
COPY --from=build /usr/src/app/target/quarkus-app/*.jar /app/
COPY --from=build /usr/src/app/target/quarkus-app/app/ /app/app/
COPY --from=build /usr/src/app/target/quarkus-app/quarkus/ /app/quarkus/

EXPOSE 8080

ENV QUARKUS_MONGODB_CONNECTION_STRING=mongodb://localhost:27017

CMD ["java", "-jar", "quarkus-run.jar"]