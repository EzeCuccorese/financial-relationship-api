FROM ubuntu:latest
LABEL authors="Ezequiel Cuccorese"

# Usar la imagen oficial de Gradle con JDK 17 desde Docker Hub
FROM gradle:7.3.1-jdk17 AS build

# Copiar el código fuente al contenedor
COPY --chown=gradle:gradle . /home/gradle/src

# Configurar el directorio de trabajo
WORKDIR /home/gradle/src

# Construir la aplicación con Gradle
RUN gradle build --no-daemon

# Segunda etapa de la construcción para ejecutar la aplicación
FROM openjdk:17-jdk

# Configurar las variables de entorno
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/reba \
    SPRING_DATASOURCE_USERNAME=rebauser \
    SPRING_DATASOURCE_PASSWORD=rebapassword

# Exponer el puerto 8080
EXPOSE 5000

# Configurar el directorio de trabajo
WORKDIR /app

# Copiar el archivo jar al contenedor
COPY --from=build /home/gradle/src/build/libs/api-reba.jar /app/

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/app/api-reba.jar"]
