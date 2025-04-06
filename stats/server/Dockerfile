FROM amazoncorretto:21
COPY target/*.jar app.jar
COPY stats-model/target/classes/ru/practicum/model /app/classes/ru/practicum/model
COPY target/stats-server.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app.jar"]