FROM gradle:8.1.1-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle

RUN gradle build --no-daemon || return 0


COPY . .

RUN gradle build --no-daemon

FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y curl

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]