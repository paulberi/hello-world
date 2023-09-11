#!/usr/bin/env bash
# exit when any command fails
set -e

#
# Första parametern vilken app som ska byggas, resten av argumenten till går till docker build, tex taggning
#

USAGE="Usage: $0 app [extra parametrar till docker build]"

if [ "$#" == "0" ]; then
	echo "$USAGE"
	exit 1
fi

app=$1
shift

# Lägg api-specar i build_tmp så att vi kan använda dom i docker containern fast dom
# ligger utanför containerns källkodsträd
rm -rf build_tmp
mkdir build_tmp
mkdir build_tmp/openapi
mkdir build_tmp/graphql
cp -r ../../openapi/* build_tmp/openapi
cp -r ../../graphql/* build_tmp/graphql

if [ -z "$https_proxy" ];
then
  https_proxy="$HTTPS_PROXY"
fi

if [ -z "$http_proxy" ];
then
  http_proxy="$HTTP_PROXY"
fi

if [ -z "$no_proxy" ];
then
  no_proxy="$NO_PROXY"
fi

githash=`git rev-parse HEAD`

export BUILDKIT_PROGRESS=plain
docker build . -f Dockerfile.generic -t xplore_${app}_frontend --build-arg APP="$app" --build-arg XPLORE_APP_VERSION="$githash" --build-arg http_proxy="$http_proxy"  --build-arg https_proxy="$https_proxy" --build-arg no_proxy="$no_proxy" "$@"

# Kopiera testrapport från container
docker create --name XPLORE_${app}_TEST xplore_${app}_frontend
docker cp XPLORE_${app}_TEST:/usr/share/test-reports/junit.xml ./junit.xml
docker rm XPLORE_${app}_TEST
# docker cp verkar bevara modifieringsdatum på filen, och Bamboo gillar inte gamla cachade testrapporter
touch junit.xml
