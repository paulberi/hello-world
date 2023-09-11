#!/bin/bash

ssh -l admin xploregeoutv01.prodstod.se "docker exec -i geoserver /bin/bash -c 'chmod -R 777 /var/geoserver_data_dir'"
scp -r admin@xploregeoutv01.prodstod.se:/home/admin/geoserver/geoserver_data_dir ../../geoserver/config/utv/.
scp -r admin@xploregeoutv01.prodstod.se:/home/admin/geoserver/gwc/geowebcache* ../../geoserver/config/utv/gwc/.