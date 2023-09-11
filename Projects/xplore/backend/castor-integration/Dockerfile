FROM openjdk:11-jre-slim
VOLUME /tmp
ENV TZ Europe/Stockholm
COPY target/castor-integration-1.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
WORKDIR /opt/workdir/

# setup proxy, required by npm install when building on bamboo
ARG HTTP_PROXY=http://webproxy.metria.net:8080
ARG HTTPS_PROXY=http://webproxy.metria.net:8080
ARG NO_PROXY=nexus.metria.se
ARG USE_PROXY=on

# sätt ENV för http_proxy med både stora och små bokstäver
ENV http_proxy ${HTTP_PROXY}
ENV https_proxy ${HTTPS_PROXY}
ENV HTTP_PROXY ${HTTP_PROXY}
ENV HTTPS_PROXY ${HTTPS_PROXY}
ENV NO_PROXY ${NO_PROXY}

#installing ca-certificates-java to import the certificate
RUN echo 'Hämtar ca-certificates-java'
RUN mkdir -p /usr/share/man/man1 \
    && apt-get update \
    && apt-get install -y ca-certificates-java

RUN echo 'kopierar certifikat för Prosonas server'
ARG CERT="cert_ekero_base64.cer"
COPY $CERT /opt/workdir/
RUN keytool -importcert -file $CERT -alias $CERT -cacerts -storepass changeit -noprompt

# Kolla så certifikatet verkligen finns med
RUN keytool -list -cacerts -alias $CERT -storepass changeit -noprompt
RUN echo 'kopierat klart, kanske'
