#!/bin/sh

# Mycket "TODO" i det här, kolla på dump.sh eller upload.sh i stället

# Default-värden
host=localhost:8080
#output=/dev/null
output=curl.log
auth=admin:geoserver

rm $output

usage() {
  echo "$0 [-o] [-h host] -w namn"
  echo "  -w namn             namn på workspace i geoservern"
  echo "  -h host:post        hostnamn för geoservern. Default är ${host}"
  echo "  -u user:password    user och password för geoservern. Default är ${auth}"
  echo "  -d                  ta bort angiven workspace i geoservern"
  echo "  -c                  skapa ny workspace med angivet namn i geoservern"
  echo "  -r                  med flaggan -d, ta bort allt innehåll i workspacen"
  echo "  -l                  testanrop, listar tillgängliga workspaces"
}



while getopts dcrlh:w:u: flag
do
  case "${flag}" in
    d) delete=1;;
    c) create=1;;
    r) recurse=1;;
    l) test_mode=1;;
    h) host=${OPTARG};;
    w) workspace=${OPTARG};;
    u) auth=${OPTARG};;
    *) usage && exit 1
  esac
done

url=http://${host}/geoserver/rest

post() {
  rest_path=$1
  shift
  curl -u "$auth" --silent --output ${output} --write-out '%{http_code}' -XPOST -H 'Content-type: application/xml' "${url}/${rest_path}" "$@"
}

get() {
  rest_path=$1
  shift
  curl -u "$auth" --silent  --output ${output} --write-out '%{http_code}' -XGET -H 'Accept: application/xml' "${url}/${rest_path}" "$@"
}

put() {
  rest_path=$1
  shift
  curl -u "$auth" --silent  --output ${output} --write-out '%{http_code}' -XPUT -H 'Content-Type: application/xml' "${url}/${rest_path}" "$@"
}

# https://docs.geoserver.org/latest/en/api/#1.0.0/workspaces.yaml
createWorkspace() {
  status=$(post /workspaces -d "<workspace><name>${1}</name></workspace>")
  case $status in
      201) echo "- 201 Workspacen skapades";;
      405) echo "- 405 Forbidden to post to an existing workspace (use PUT)" && return 1;;
      *) echo "- $status" && return 1
  esac
}

# https://docs.geoserver.org/latest/en/api/#1.0.0/datastores.yaml
createDatastore() {
  _workspace=$1
  _file=$2
  status=$(post "/workspaces/$_workspace/datastores" -d @"$_file")
  case $status in
    201) echo "- 201 Created";;
      *) echo "- $status" && return 1
  esac
}

createFeatureType() {
  _workspace=$1
  _datastore=$2
  _file=$3
  status=$(post "/workspaces/$_workspace/datastores/$_datastore/featuretypes" -d @"$_file")
  case $status in
    201) echo "- 201 The feature type was successfully created.";;
      *) echo "- $status" && return 1
  esac
}


if [ -n "$test_mode" ]; then
  curl -u admin:geoserver -XGET -H "Accept: application/xml"  "${url}/workspaces"
  echo
  exit 0
fi

if [ -z "$workspace" ]; then
  echo "No workspace specified" && usage && exit 1
fi

# Ta bort nuvarande workspace
if [ -n "${delete}" ]; then
  rest_endpoint=${url}/workspaces/${workspace}
  if [ -n "$recurse" ]; then
    echo "Tar bort workspace ${workspace} med allt innehåll"
    rest_endpoint="${rest_endpoint}?recurse=true"
  else
    echo "Tar bort workspace ${workspace}"
  fi
  status=$(curl "$rest_endpoint" --silent --output ${output} --write-out "%{http_code}" -u admin:geoserver \
    -XDELETE -H "Content-type: application/xml" -d "<workspace><name>${workspace}</name></workspace>")
  case $status in
      # Statuskoder från https://docs.geoserver.org/latest/en/api/#1.0.0/workspaces.yaml
      200) echo "- 200 - Success workspace deleted";;
      403) echo "- 403 - Workspace or related Namespace is not empty (and recurse not true)" && exit 1;;
      404) echo "- 404 - Workspace doesn’t exist";;
      405) echo "- 405 - Can’t delete default workspace" && exit 1;;
      *) echo "- $status" && exit 1;;
  esac
fi

# Skapa workspace
if [ -n "${create}" ]; then
  echo "Skapar workspace $workspace"
  createWorkspace "$workspace" || exit 1
else
  exit 0
fi

# Datastores
echo "Skapar data stores"
createDatastore "$workspace" datastore.xml || exit 1

# Feature types
echo Skapar feature types
for file in featuretypes/*.xml; do
  featuretype=$(basename -s .xml "${file}")
  echo "Creating $featuretype:"
  createFeatureType "$workspace" markkoll_postgis "$file"
done

# Layers
#rest_endpoint=$url/workspaces/$workspace/layers
#curl "$rest_endpoint" -v -u admin:geoserver -XPOST -H "Content-type: application/xml" -d @
