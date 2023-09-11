FROM openjdk:11-jre-slim
VOLUME /tmp
ENV TZ Europe/Stockholm

RUN sed -i -e's/ main/ main contrib non-free/g' /etc/apt/sources.list && apt-get -q update
RUN apt-get install -y curl

# Fonts för export till Haglöf
RUN apt-get install -y fontconfig libfreetype6

# Microsofts typsnitt, ex. Arial, för rendering av text i skalstock
RUN echo "ttf-mscorefonts-installer msttcorefonts/accepted-mscorefonts-eula select true" | debconf-set-selections
RUN apt-get install -yyq ttf-mscorefonts-installer

COPY target/markkoll-0.0.1-SNAPSHOT.jar app.jar

HEALTHCHECK \
    --retries=1 \
    --start-period=180s \
    CMD curl --fail -v 'http://localhost:9011/actuator/health' || exit 1
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
