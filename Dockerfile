FROM eclipse-temurin:21

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENV PORT=8080
ENV SERVER_PORT=8080
ENV SERVER_ADDRESS=0.0.0.0

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT}"]
