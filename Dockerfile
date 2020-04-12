FROM openjdk:11-jdk-slim
ARG JAR_FILE=application/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]