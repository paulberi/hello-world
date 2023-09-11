#!/bin/bash

CATALINA_OPTS="-DGEOSERVER_DATA_DIR=${GEOSERVER_DATA_DIR} -DGEOSERVER_LOG_LOCATION=${GEOSERVER_DATA_DIR}/logs/geoserver.log -DGEOWEBCACHE_CACHE_DIR=${GEOWEBCACHE_CACHE_DIR}  -Djava.net.preferIPv4Stack=true"

CATALINA_OPTS="$CATALINA_OPTS -server -Xms1g -Xmx2g -Djava.awt.headless=true"

if [[ -z "${ELASTIC_APM_SERVER_URL}" ]]; then
  echo "ELASTIC_APM_SERVER_URL not set, no tracing configured"
  # No APM, do nothing
else
  if [[ -z "${ELASTIC_APM_SERVICE_NAME}" ]]; then
    export ELASTIC_APM_SERVICE_NAME="geoserver"
  fi

  export CATALINA_OPTS="$CATALINA_OPTS -javaagent:/elastic-apm-agent.jar"
  export CATALINA_OPTS="$CATALINA_OPTS -Delastic.apm.application_packages=org.geoserver,org.geotools"
  export CATALINA_OPTS="$CATALINA_OPTS -Delastic.apm.log_ecs_reformatting=OVERRIDE"
fi
