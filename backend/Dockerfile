FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Copiamos el .jar generado por Maven al contenedor
COPY target/todo-list-0.0.1-SNAPSHOT.jar app.jar
# Exponemos el puerto 8080 (donde corre Spring Boot)
EXPOSE 8080
# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
