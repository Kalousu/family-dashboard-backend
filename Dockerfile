# ==========================================
# Stage 1: Das Java-Projekt bauen (Maven)
# ==========================================
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Kopiere zuerst nur die pom.xml und lade die Abhängigkeiten herunter
COPY pom.xml .
RUN mvn dependency:go-offline

# Kopiere jetzt den restlichen Code und baue das Projekt (ohne Tests)
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# Stage 2: Die fertige App ausführen
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Wir kopieren die fertige .jar-Datei aus Stage 1 in unser finales Image
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Der Befehl, um die App zu starten
ENTRYPOINT ["java", "-jar", "app.jar"]