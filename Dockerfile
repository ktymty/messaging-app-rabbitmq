FROM openjdk:8-jre-slim

LABEL maintainer="kirtymohanty"

ARG JAR_FILE=target/*.jar

VOLUME /tmp

COPY ${JAR_FILE} messaging-app.jar

ENTRYPOINT ["java","-jar","messaging-app.jar"]