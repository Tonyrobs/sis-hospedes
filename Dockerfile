# Estágio 1: Build da aplicação usando Maven e Java 17 (Temurin)
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Execução da aplicação com uma imagem Java 17 atualizada e segura
FROM eclipse-temurin:17-jre-slim
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]