# Usamos una imagen de Java 17 (la versión que tienes en tu proyecto)
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR generado por Maven al contenedor
COPY target/*.jar app.jar

# Exponemos el puerto que usa Render
EXPOSE 10000

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=10000"]