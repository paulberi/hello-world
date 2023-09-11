FROM tomcat:9-jdk11-openjdk-bullseye

ARG GS_VERSION=2.20.4
ARG GS_COMMUNITY_PLUGINS_VERSION=2.20
ARG GEOSERVER_WAR_URL=http://downloads.sourceforge.net/project/geoserver/GeoServer/${GS_VERSION}/geoserver-${GS_VERSION}-war.zip
ENV GEOSERVER_DATA_DIR=/var/geoserver_data_dir
ENV GEOSERVER_VERSION=${GS_VERSION}
ENV GEOWEBCACHE_CACHE_DIR=/var/gwc

RUN mkdir -p ${GEOSERVER_DATA_DIR}

RUN \
    sed -i -e's/ main/ main contrib non-free/g' /etc/apt/sources.list \
    && apt-get -q update

RUN apt-get update && apt-get install -y \
  fonts-dejavu zip

RUN echo "ttf-mscorefonts-installer msttcorefonts/accepted-mscorefonts-eula select true" | debconf-set-selections
RUN apt-get install -yyq fonts-liberation ttf-mscorefonts-installer

RUN wget https://search.maven.org/remotecontent?filepath=co/elastic/apm/elastic-apm-agent/1.30.1/elastic-apm-agent-1.30.1.jar -O /elastic-apm-agent.jar

# Uncomment if the data_dir should be added to the image
#ADD geoserver_data_dir ${GEOSERVER_DATA_DIR}
# apk add --update unzip zip wget ca-certificates \
RUN \
  # Prefer ttf-dejavu to ttf-freefont/droid/ubuntu-font-family/liberation, but it is a matter of taste.
  # ttf-dejavu tzdata && \
  # update-ca-certificates && \
  wget ${GEOSERVER_WAR_URL} -O /tmp/geoserver.zip && \
  unzip /tmp/geoserver.zip -d /tmp && \
  # Unzip the war and deploy it exploded to allow for adding jars into geoserver/WEB_INF/lib etc.
  mkdir ${CATALINA_HOME}/webapps/geoserver && \
  unzip -q /tmp/geoserver.war -d ${CATALINA_HOME}/webapps/geoserver && \
  # Remove default data-dir - setenv.sh points to GEOSERVER_DATA_DIR
  rm -rf ${CATALINA_HOME}/webapps/geoserver/data && \
  # Remove downloads
  rm /tmp/geoserver.*

# Add plugins (i.e. community modules)
ADD add-plugins.sh /tmp/add-plugins.sh
RUN chmod +x /tmp/add-plugins.sh
RUN /tmp/add-plugins.sh && rm /tmp/add-plugins.sh

ENV TZ Europe/Stockholm

# Set environment for tomcat via setenv.sh (Java options and GEOSERVER_DATA_DIR)
ADD setenv.sh ${CATALINA_HOME}/bin/
RUN chmod +x ${CATALINA_HOME}/bin/setenv.sh

# Setup admin user (admin/go4it!)
ADD tomcat-conf ${CATALINA_HOME}/conf/

# Anpassad context.xml för att kunna komma åt manager
ADD tomcat-conf ${CATALINA_HOME}/webapps/manager/META-INF

# Lägg till proxy conf i catalina.properties
RUN echo "http.proxyHost=webproxy.metria.net" >> ${CATALINA_HOME}/conf/catalina.properties
RUN echo "http.proxyPort=8080" >> ${CATALINA_HOME}/conf/catalina.properties
RUN echo "https.proxyHost=webproxy.metria.net" >> ${CATALINA_HOME}/conf/catalina.properties
RUN echo "https.proxyPort=8080" >> ${CATALINA_HOME}/conf/catalina.properties
RUN echo 'http.nonProxyHosts=localhost|127.*|[::1]|*.vic-metria.nu|*.metria.net|*.metria.se|*.prodstod.se' >> ${CATALINA_HOME}/conf/catalina.properties
RUN echo 'https.nonProxyHosts=localhost|127.*|[::1]|*.vic-metria.nu|*.metria.net|*.metria.se|*.prodstod.se' >> ${CATALINA_HOME}/conf/catalina.properties
RUN echo 'http.proxySet=true' >> ${CATALINA_HOME}/conf/catalina.properties

# Anpassad applicationSecurityContext.xml för att kunna läsa av xplore-roller korrekt från nginx:en
RUN unzip -o -j -d . ${CATALINA_HOME}/webapps/geoserver/WEB-INF/lib/gs-main-${GS_VERSION}.jar applicationSecurityContext.xml
RUN sed -i 's/<property name="roleDelimiterString" value=";"/<property name="roleDelimiterString" value=","/g' applicationSecurityContext.xml
RUN sed -i 's/<property name="roleParameterDelimiterString" value=","/<property name="roleParameterDelimiterString" value=";"/g' applicationSecurityContext.xml
RUN zip -u ${CATALINA_HOME}/webapps/geoserver/WEB-INF/lib/gs-main-${GS_VERSION}.jar applicationSecurityContext.xml

HEALTHCHECK \
    --retries=3 \
    --start-period=360s \
    CMD curl --fail -v http://localhost:8080/geoserver/index.html || exit 1
CMD ["catalina.sh", "run"]
