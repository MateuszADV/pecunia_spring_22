# 1. Obraz z Mavenem i JDK do budowy aplikacji
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Kopiujemy projekt i budujemy JAR
COPY . .
RUN mvn clean package -DskipTests

# 2. Lżejszy obraz JDK do uruchomienia aplikacji
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Kopiujemy zbudowany plik JAR z poprzedniego etapu
COPY --from=build /app/target/*.jar app.jar

# Otwieramy port aplikacji Spring Boot
EXPOSE 8080

# Uruchamiamy aplikację
ENTRYPOINT ["java", "-jar", "app.jar"]
