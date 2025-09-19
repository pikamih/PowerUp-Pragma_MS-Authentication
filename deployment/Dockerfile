# Imagen base con JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Crear directorios para la app y keys
RUN mkdir -p /app /keys

# Copiar el JAR generado por Gradle
COPY applications/app-service/build/libs/MS_Authentication.jar /app/MS_Authentication.jar

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Crear un usuario no-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Definir variables de entorno para JWT
ENV JWT_PRAGMA_PRIVATE_KEY_PATH=/keys/private_key.pem
ENV JWT_PRAGMA_PUBLIC_KEY_PATH=/keys/public_key.pem

# Puerto que expone la app
EXPOSE 8080

# Opciones de JVM
ENV JAVA_OPTS="-Xmx512m -Djava.security.egd=file:/dev/./urandom"

# Copiar el script al contenedor
COPY deployment/wait-for-it.sh /wait-for-it.sh

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar MS_Authentication.jar"]
