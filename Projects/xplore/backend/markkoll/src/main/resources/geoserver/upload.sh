#!/bin/bash
####################################
## Standardparametrar för MarkKoll #
####################################

# HOST_PROD=xploreprod01.prodstod.se:9018
HOST_PROD=localhost:9018
DATASTORE_HOST_PROD=xploredb02.prodstod.se
DATASTORE_DATABASE_PROD=markkoll

# HOST_ACC=xploreacc01.prodstod.se:9018
HOST_ACC=localhost:9018
DATASTORE_HOST_ACC=xploreaccdb01.prodstod.se
DATASTORE_DATABASE_ACC=markkoll

HOST_TEST=xploretst01.prodstod.se:9018
DATASTORE_HOST_TEST=xploredb01.prodstod.se
DATASTORE_DATABASE_TEST=markkoll_test

HOST_UTV=xploreutv01.prodstod.se:9017
DATASTORE_HOST_UTV=pgtest.prodstod.se
DATASTORE_DATABASE_UTV=markkoll_utv

HOST_LOCAL=localhost:8080
DATASTORE_HOST_LOCAL=192.168.50.9
DATASTORE_DATABASE_LOCAL=markkoll

usage() {
  echo "$0 -u user:password -d user:password -n database [-g addr:port] [-h addr] ENV:"
  echo "        -u user:password"
  echo "           inloggningsuppgifter för geoserver"
  echo "        -d user:password"
  echo "           inloggningsuppgifter för databasen"
  echo "        -g addr:port"
  echo "            override på adressen til din datastore"
  echo "        -h addr"
  echo "            override på adressen til din geoserver"
  echo "        ENV är en av"
  echo "        prod Produktionsmiljön"
  echo "           host: $HOST_PROD"
  echo "           datastore_host: $DATASTORE_HOST_PROD"
  echo "        acc Acctestmiljön"
  echo "           host: $HOST_ACC"
  echo "           datastore_host: $DATASTORE_HOST_ACC"
  echo "        test Testmiljön"
  echo "           host: $HOST_TEST"
  echo "           datastore_host: $DATASTORE_HOST_TEST"
  echo "        utv Utvecklingsmiljön"
  echo "           host: $HOST_UTV"
  echo "           datastore_host: $DATASTORE_HOST_UTV"
  echo "        local Lokal miljö"
  echo "           host: $HOST_LOCAL"
  echo "           datastore_host: $DATASTORE_HOST_LOCAL"
}



while getopts u:d:g:h: flag
do
  case "${flag}" in
    u) AUTH_GEOSERVER=${OPTARG};;
    d) AUTH_DB=${OPTARG};;
    g) OVERRIDE_GEOSERVER_HOST=${OPTARG};;
    h) OVERRIDE_DB_HOST=${OPTARG};;
    *) usage && exit 1
  esac
done

if [ -z "$AUTH_GEOSERVER" ]; then
  usage && exit 1
fi
if  [ -z "$AUTH_DB" ]; then
  usage && exit 1
else
  datastore_user=$(cut -s -d : -f 1 <<<"$AUTH_DB")
  datastore_passwd=$(cut -s -d : -f 2 <<<"$AUTH_DB")
  if [ -z "$datastore_user" ] || [ -z "$datastore_passwd" ]; then
    usage && exit 1
  fi
fi

shift $(( OPTIND - 1 ))
case "$1" in
  prod) echo "Prod!"
    host=$HOST_PROD
    datastore_host=$DATASTORE_HOST_PROD
    datastore_database=$DATASTORE_DATABASE_PROD
    ;;
  acc) echo "Acc!"
    host=$HOST_ACC
    datastore_host=$DATASTORE_HOST_ACC
    datastore_database=$DATASTORE_DATABASE_ACC
    ;;
  test) echo "Test!"
    host=$HOST_TEST
    datastore_host=$DATASTORE_HOST_TEST
    datastore_database=$DATASTORE_DATABASE_TEST
    ;;
  utv) echo "Utv!"
    host=$HOST_UTV
    datastore_host=$DATASTORE_HOST_UTV
    datastore_database=$DATASTORE_DATABASE_UTV
    ;;
  local) echo "Local!"
    host=$HOST_LOCAL
    datastore_host=$DATASTORE_HOST_LOCAL
    datastore_database=$DATASTORE_DATABASE_LOCAL
    ;;
  *) echo "Okänd env: $1" && usage && exit 1;;
esac

if [ -n "$OVERRIDE_GEOSERVER_HOST" ]; then
    host=$OVERRIDE_GEOSERVER_HOST
fi
if [ -n "$OVERRIDE_DB_HOST" ]; then
    datastore_host=$OVERRIDE_DB_HOST
fi

url=http://${host}/geoserver/rest

echo Using Geoserver at $url
echo Connecting to Geoserver as: "$AUTH_GEOSERVER"
echo Using datastore at $datastore_host
echo Connect to datastore as "$datastore_user $datastore_passwd"

delete() {
  rest_path=$1
  shift
  curl -u "$AUTH_GEOSERVER" -XDELETE "${url}${rest_path}" "$@"
}

post_xml() {
  rest_path=$1
  shift
  curl -u "$AUTH_GEOSERVER" -XPOST -H "Content-Type: application/xml" "${url}${rest_path}" "$@"
}

put() {
  rest_path=$1
  shift
  curl -u "$AUTH_GEOSERVER" -XPUT "${url}${rest_path}" "$@"
}

put_xml() {
  put "$@" -H "Content-Type: application/xml"
}

put_sld() {
  put "$@" -H "Content-Type: application/vnd.ogc.sld+xml"
}

workspaces=$(find workspaces -mindepth 1 -maxdepth 1 -type d -print0 | xargs -0 -n 1 basename)
#workspaces=markkoll_extern
for workspace in $workspaces; do
  echo Processing workspace $workspace
  ws_root="workspaces/$workspace"
  echo "Deleting workspace on $host"
  delete "/$ws_root?recurse=true"

  echo "Posting workspace to $host"
  post_xml /workspaces -d "<workspace><name>$workspace</name></workspace>"

  datastores=$(find "$ws_root/datastores" -mindepth 1 -maxdepth 1 -type d -print0 | xargs -0 -n 1 basename)
  for datastore in $datastores; do
    echo "Updating datastore $datastore"
    ds_root=$ws_root/datastores/$datastore
    ds_file=$ds_root/datastore.xml
    # borde nog escapa host-strängen
    sed -i'' -e 's/\(<entry key="host">\).*\(<\/entry>\)/\1'$datastore_host'\2/' "$ds_file"
    sed -i'' -e 's/\(<entry key="user">\).*\(<\/entry>\)/\1'$datastore_user'\2/' "$ds_file"
    sed -i'' -e 's/\(<entry key="passwd">\).*\(<\/entry>\)/\1'$datastore_passwd'\2/' "$ds_file"
    sed -i'' -e 's/\(<entry key="database">\).*\(<\/entry>\)/\1'$datastore_database'\2/' "$ds_file"
    # Hårdkodar namespace, hittar inte hur man sätter detta när man skapar workspace. Den blir default http://$workspace.
    # Normalt när man publicerar på Geoservern sätter man en mer riktig workspace, t ex http://markkoll.metria.se/. Sätter
    # man den så så ärvs det ner till datastore-specifikationen. Förstår inte hur/varför det används på datastoren, men det
    # gör att det inte funkar för vissa skikt. På det här sättet blir det i alla fall konsekvent i det som laddas upp. Byter
    # man namespace på workspacen i Geoservern senare slår det igenom på datastores automatiskt.
    sed -i'' -e 's/\(<entry key="namespace">\).*\(<\/entry>\)/\1http:\/\/'$workspace'\2/' "$ds_file"

    echo "Posting datastore to $host"
    post_xml "/$ws_root/datastores" -d @"$ds_file"


    features=$(find "$ds_root/featuretypes" -maxdepth 1 -type f -print0 | xargs -0 -n 1 basename)
    for feature in $features; do
      echo "Posting featuretype $feature to $host"
      post_xml "/$ws_root/datastores/$datastore/featuretypes" -d @"$ds_root/featuretypes/$feature"
    done

    # TODO: Support för andra content-types än svg? Kan man hämta metadata från käll-geoservern?
    if [ -d resource/$ws_root ]; then
      resources=$(find "resource/$ws_root" -type f -name \*.svg)
      for file in $resources; do
        res_name=$(basename "$file")
        echo "Putting SVG resource $res_name to $host"
        put "/resource/$ws_root/styles/$res_name" -H "Content-Type: image/svg+xml" -d @"$file"
      done
    else
      echo "No resources in workspace"
    fi

    styles=$(find "$ws_root/styles" -type f -name \*.xml -print0 | xargs -0 -n 1 basename -s .xml)
    for style in $styles; do
      echo "Posting style $style (XML) to $host"
      post_xml /$ws_root/styles -d @"$ws_root/styles/$style.xml"
      echo "Putting style $style (SLD) to $host"
      put_sld "/$ws_root/styles/$style" -d @"$ws_root/styles/$style.sld"
    done

    layers=$(find "$ws_root/layers" -type f -print0 -name \*.xml | xargs -0 -n 1 basename -s .xml)
    for layer in $layers; do
      echo "Putting layer $layer to $host"
      put_xml "/$ws_root/layers/$layer" -d @"$ws_root/layers/$layer.xml"
    done

  if [ -d "$ws_root/layergroups" ]; then
    layergroups=$(find "$ws_root/layergroups" -type f -print0 -name \*.xml | xargs -0 -n 1 basename -s .xml)
    for layergroup in $layergroups; do
      echo "Posting layergroup $layergroup to $host"
      post_xml "/$ws_root/layergroups" -d @"$ws_root/layergroups/$layergroup.xml"
    done
  fi
  done
done
