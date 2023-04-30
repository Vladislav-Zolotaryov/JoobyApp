FROM gradle:8.1.1-jdk17 as build
WORKDIR /JoobyApp
COPY build.gradle.kts build.gradle
COPY settings.gradle settings.gradle
COPY src src
COPY conf conf
RUN gradle shadowJar

FROM openjdk:17-jdk-slim
WORKDIR /JoobyApp
COPY --from=build /JoobyApp/build/libs/JoobyApp-1.0.0-all.jar app.jar
COPY conf conf
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
