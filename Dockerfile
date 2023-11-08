FROM gradle:8.3.0-jdk17-alpine AS builder
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME
COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src
RUN gradle build || return 0
COPY . .
RUN gradle clean build -x test

FROM eclipse-temurin:17-jre-alpine
ENV APP_HOME=/app
ENV ARTIFACT_NAME=app-0.0.1-SNAPSHOT.jar
WORKDIR $APP_HOME
COPY --from=builder $APP_HOME/build/libs/$ARTIFACT_NAME .
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}
