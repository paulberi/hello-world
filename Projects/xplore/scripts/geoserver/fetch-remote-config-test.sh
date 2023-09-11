#!/bin/bash

ssh -l admin xploregeotst01.prodstod.se "docker exec -i geoserver /bin/bash -c 'chmod -R 777 /var/geoserver_data_dir'"
scp -r admin@xploregeotst01.prodstod.se:/home/admin/geoserver/geoserver_data_dir ../../geoserver/config/test/.
scp -r admin@xploregeotst01.prodstod.se:/home/admin/geoserver/gwc/geowebcache* ../../geoserver/config/test/gwc/.
