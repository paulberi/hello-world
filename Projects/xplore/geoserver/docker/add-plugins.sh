#!/bin/bash
# exit when any command fails
set -e

plugins=(vectortiles)

if [[ ${#plugins[@]} != 0 ]]; then
  mkdir -p /tmp/plugins

  for i in "${plugins[@]}"
  do
    wget -nv -c http://downloads.sourceforge.net/project/geoserver/GeoServer/${GS_VERSION}/extensions/geoserver-${GS_VERSION}-${i}-plugin.zip -O /tmp/plugins/${i}.zip
  done

  for p in /tmp/plugins/*.zip;
  do
    unzip $p -d /tmp/gs_plugin && \
    mv /tmp/gs_plugin/*.jar ${CATALINA_HOME}/webapps/geoserver/WEB-INF/lib/ && \
    rm -rf /tmp/gs_plugin
  done

  rm -rf /tmp/plugins
fi
