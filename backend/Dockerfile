# ETAPA 1: Construcción (Usando una imagen oficial de Maven)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiamos todo el código fuente al contenedor
COPY . .

# Usamos el comando nativo de Maven para compilar y generar el .jar
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución (Imagen final súper ligera)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copiamos SOLO el dsss.jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]