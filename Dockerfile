
FROM gradle:jdk11 as build

ADD build/libs/user-service-0.0.1-SNAPSHOT.jar user-service.jar

COPY ./gradle ./gradle
COPY ./gradlew ./gradlew
COPY ./gradlew.bat ./gradlew.bat
COPY ./settings.gradle ./settings.gradle


EXPOSE 8080