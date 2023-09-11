FROM openjdk:11-jre-slim
VOLUME /tmp

RUN apt-get -q update
RUN apt-get install -y curl

COPY target/skyddade-omraden-1.0.jar app.jar

HEALTHCHECK \
    --retries=1 \
    --start-period=30s \
    CMD curl --fail -v 'http://localhost:9020/actuator/health' || exit 1
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
