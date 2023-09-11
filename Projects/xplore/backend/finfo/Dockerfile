FROM openjdk:11-jre-slim
VOLUME /tmp
ENV TZ Europe/Stockholm

RUN apt-get -q update
RUN apt-get install -y curl

COPY target/finfo-1.0.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
