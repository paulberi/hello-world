#!/bin/bash

ssh -l bamboodeploy xploregeoprod01.prodstod.se "docker exec -i geoserver /bin/bash -c 'chmod -R 777 /var/geoserver_data_dir'"
scp -r bamboodeploy@xploregeoprod01.prodstod.se:/home/bamboodeploy/geoserver/geoserver_data_dir ../../geoserver/config/prod/.
scp -r bamboodeploy@xploregeoprod01.prodstod.se:/home/bamboodeploy/geoserver/gwc/geowebcache* ../../geoserver/config/prod/gwc/.