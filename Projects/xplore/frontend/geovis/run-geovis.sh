#!/bin/bash

if   [ -n "$1" ] && [ $1 = "odeshog" ]; then PORT=8082; HOST="demo";
elif [ -n "$1" ] && [ $1 = "mullsjo" ]; then PORT=8083; HOST="mullsjo";
elif [ -n "$1" ] && [ $1 = "aneby" ];   then PORT=8084; HOST="aneby";
else
    echo "Kommun saknas eller är felaktig. Tillåtna värden: odeshog, mullsjo, aneby"
    exit;
fi

if [ -n "$2" ] && [ $2 = "geoserver" ]; then
    (docker ps -a | grep 'xplore/geoserver') && (docker rm -f geoserver)
    docker run --name geoserver -p 8080:8080 -v "$PWD"/../../geoserver/geovis/geoserver_data_dir:/var/geoserver_data_dir -d nexus.metria.se:5000/xplore/geoserver:2.14.0
    GEOSERVER="http\:\/\/localhost\:8080\/geoserver"
else
    GEOSERVER="http\:\/\/xploregeoutv01.prodstod.se\:8080\/geoserver"
fi

(docker ps -a | grep 'xplore/geovis-'$1) && (docker rm -f geovis-$1)
(docker images | grep 'xplore/geovis-'$1) && (docker image rm -f nexus.metria.se:5000/xplore/geovis-$1)

docker run --name geovis-$1 -p $PORT:80 -d nexus.metria.se:5000/xplore/geovis-$1

docker exec -i -t geovis-$1 /bin/sh -c "sed -i \"s/\/geosok/http\:\/\/geovis-"$HOST"-utv.metria.se\/geosok/g\" /usr/share/nginx/html/index.json"
docker exec -i -t geovis-$1 /bin/sh -c "sed -i \"s/\/geoserver/"$GEOSERVER"/g\" /usr/share/nginx/html/index.json"

echo "************************************************"
echo "*                                              *"
echo "*   Geovis körs nu på: http://localhost:"$PORT"   *"
echo "*                                              *"
echo "************************************************"
