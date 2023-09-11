#!/bin/bash

usage() {
  echo "Giltiga parametrar:"
  echo "  -h host:port        adress för geoservern."
  echo "  -u user:password    user och password för geoservern."
  echo "  -w namn             namn på workspace i geoservern"
  echo "  -d datastore        namn på den datastore som ska användas (enbart en stöds för tillfället)"
  echo "  -f featurelist      lista av featuretypes som ska hämtas"
  echo "  -s styles           lista av styles som ska hämtas"
  echo "  -l layers           lista av layers som ska hämtas"
  echo "  -g layergroups      lista av layergroups som ska hämtas"
  echo "  -r resources        lista av resources som ska hämtas"
}

while getopts h:u:w:d:f:s:l:g:r: flag
do
  case "${flag}" in
    h) host=${OPTARG};;
    u) auth=${OPTARG};;
    w) workspace=${OPTARG};;
    d) datastore=${OPTARG};;
    f) featurelist=${OPTARG};;
    s) styles=${OPTARG};;
    l) layers=${OPTARG};;
    g) layergroups=${OPTARG};;
    r) resources=${OPTARG};;
    *) usage && exit 1
  esac
done

if [ -z "$host" ]; then
  echo "Ingen host specificerad" && usage && exit 1
fi

if [ -z "$auth" ]; then
  echo "Ingen auth specificerad" && usage && exit 1
fi

if [ -z "$workspace" ]; then
  echo "Ingen workspace specificerad" && usage && exit 1
fi

if [ -z "$datastore" ]; then
  echo "Ingen datastore specificerad" && usage && exit 1
fi

if [ -z "$featurelist" ]; then
  echo "Ingen featurelist specificerad" && usage && exit 1
fi

if [ -z "$layers" ]; then
  echo "Inga layers specificerade, använder featurelist ($featurelist)"
fi

if [ -z "$styles" ]; then
  echo "Inga styles specificerade, använder layers ($layers)"
  styles="$layers"
fi

url=http://${host}/geoserver/rest

get() {
  rest_path=$1
  shift
  curl -u "$auth" -XGET  "${url}/${rest_path}" "$@"

}

get_xml() {
  get "$@" -H "Accept: application/xml"
}

get_sld() {
  get "$@" -H "Accept: application/vnd.ogc.sld+xml"
}

ws_root=workspaces/$workspace
ds_root=$ws_root/datastores/$datastore

mkdir -p "$ds_root"

get_xml "/$ds_root" > "$ds_root/datastore.xml"
sed -i'' -e '/<workspace>/,/<\/workspace>/d' "$ds_root/datastore.xml"
sed -i'' -e '/<featureTypes>/,/<\/featureTypes>/d' "$ds_root/datastore.xml"
sed -i'' -e '/<__default>/d' "$ds_root/datastore.xml"
sed -i'' -e '/<dateCreated>/d' "$ds_root/datastore.xml"
sed -i'' -e '/<dateModified>/d' "$ds_root/datastore.xml"

mkdir -p "$ds_root/featuretypes"
for feature in $featurelist; do
  get_xml "/$ds_root/featuretypes/$feature.xml" | sed -e '/<atom:link/d' > "$ds_root/featuretypes/$feature.xml"
  sed -i'' -e '/<namespace>/,/<\/namespace>/d' "$ds_root/featuretypes/$feature.xml"
  sed -i'' -e '/<dateModified>/d' "$ds_root/featuretypes/$feature.xml"
done

mkdir -p "$ws_root/styles"
for style in $styles; do
  resource=$ws_root/styles/$style
  get_sld "/$resource" > "$resource.sld"
  get_xml "/$resource" > "$resource.xml"
  sed -i'' -e '/<dateModified>/d' "$resource.xml"
done

mkdir -p "$ws_root/layers"
for layer in $layers; do
  resource=$ws_root/layers/$layer
  get_xml "/$resource" > "$resource.xml"
  sed -i'' -e '/<workspace>/d' "$resource.xml"
  sed -i'' -e '/<atom:link/d' "$resource.xml"
  sed -i'' -e '/<dateModified>/d' "$resource.xml"
done

# TODO: Kan man hämta content-type och annan metadata?
if [ -n "${resources}" ]; then
  mkdir -p "resource/$ws_root"
  for file in $resources; do
    resource="resource/$ws_root/$file"
    mkdir -p "$(dirname $resource)"
    get "/$resource" > "$resource"
  done
fi

if [ -n "${layergroups}" ]; then
  mkdir -p "$ws_root/layergroups"
  for layergroup in $layergroups; do
    echo "Hämtar layergroup $layergroup"
    resource=$ws_root/layergroups/$layergroup
    get_xml "/$resource" > "$resource.xml"
    sed -i'' -e '/<workspace>/,/<\/workspace>/d' "$resource.xml"
    sed -i'' -e '/<atom:link/d' "$resource.xml"
  done
fi
