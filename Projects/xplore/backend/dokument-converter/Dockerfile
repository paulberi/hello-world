FROM openjdk:11-jre-slim
VOLUME /tmp
ENV TZ Europe/Stockholm

USER root

RUN \
    sed -i -e's/ main/ main contrib non-free/g' /etc/apt/sources.list \
    && apt-get -q update

RUN apt-get install -y libreoffice
RUN apt-get install -y procps
RUN apt-get install -y curl

COPY target/dokument-converter-0.0.1-SNAPSHOT.jar app.jar

HEALTHCHECK \
    --retries=1 \
    --start-period=180s \
    CMD curl --fail -v 'http://localhost:9019/actuator/health' || exit 1
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
