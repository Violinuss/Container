# Erster Schritt: Verwende Gradle-JDK-Image für den Build
FROM gradle:8.1.1-jdk17 AS builder

# Arbeitsverzeichnis setzen
WORKDIR /app

# Kopiere die Gradle- und Projektdateien in das Arbeitsverzeichnis
COPY build.gradle.kts settings.gradle.kts ./

# Build ohne Daemon ausführen
RUN gradle build --no-daemon

# Zweiter Schritt: Verwende ein schlankes OpenJDK-Image
FROM openjdk:17-jdk-slim

# Update und curl installieren (optional)
RUN apt-get update && apt-get install -y curl

# Arbeitsverzeichnis setzen
WORKDIR /app

# Kopiere das gebaute Jar aus dem Builder-Container in das schlanke Image
COPY --from=builder /app/build/libs/*.jar app.jar

# Port freigeben
EXPOSE 8080

# Startbefehl
ENTRYPOINT ["java", "-jar", "app.jar"]
