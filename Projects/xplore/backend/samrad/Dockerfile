FROM openjdk:11-jre-slim
VOLUME /tmp

RUN apt-get -q update
RUN apt-get install -y curl

COPY target/samrad-0.0.1-SNAPSHOT.jar app.jar

HEALTHCHECK \
    --retries=1 \
    --start-period=180s \
    CMD curl --fail -v 'http://localhost:9025/actuator/health' || exit 1
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]