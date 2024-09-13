FROM amazoncorretto:17-alpine-jdk

COPY target/Restaurante-0.0.1-SNAPSHOT.jar app.jar
COPY images /app/images

# Crear el directorio para las imágenes y copiar los archivos
RUN mkdir -p uploads/images
COPY uploads/images/ uploads/images/
ENTRYPOINT ["java","-jar","/app.jar"]

