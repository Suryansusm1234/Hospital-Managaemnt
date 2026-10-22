FROM eclipse-temurin:21-jdk-noble
WORKDIR /hospital-project
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src
RUN ./gradlew bootJar --no-daemon

#Stage 2
FROM eclipse-temurin:21-jre-noble
LABEL authors="suryansu"
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
