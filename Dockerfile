# Build
FROM mcr.microsoft.com/openjdk/jdk:17-mariner AS build
COPY . .
# Copia o código e roda o build do Maven. Se você usa o wrapper do maven (mvnw), altere para: RUN ./mvnw clean package -DskipTests
RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests

# Execução
FROM public.ecr.aws/amazoncorretto/amazoncorretto:17-alpine
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]