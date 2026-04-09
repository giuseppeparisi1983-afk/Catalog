# 1. Fase di compilazione (Build)
# Usiamo Maven con JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamo i file di configurazione
COPY pom.xml .
# Copiamo il codice sorgente
COPY src ./src

# Compiliamo il progetto con il profilo production di Vaadin
# Questo genererà il file .jar nella cartella target
RUN mvn clean package -Pproduction -DskipTests

# 2. Fase di esecuzione (Run)
# Usiamo un JRE 21 leggero per far girare l'app
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiamo il file jar dalla fase di build
COPY --from=build /app/target/*.jar app.jar

# Espone la porta standard di Spring Boot
EXPOSE 8080

# Comando per avviare l'app
ENTRYPOINT ["java", "-jar", "app.jar"]