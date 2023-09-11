#!/usr/bin/env bash
# exit when any command fails
set -e

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

image_tag="xplore_designsystem_frontend"
conatiner_name=image_tag

export BUILDKIT_PROGRESS=plain
docker build . -f Dockerfile.designsystem -t $image_tag --build-arg http_proxy="$http_proxy"  --build-arg https_proxy="$https_proxy" --build-arg no_proxy="$no_proxy" "$@"

set +e
docker rm $conatiner_name > /dev/null 2>&1
set -e

# Kopiera testrapport från container
docker create --name $conatiner_name $image_tag

# Faila inte på följande kommandon så att tillfälliga containern rensas i alla lägen
set +e

docker cp $conatiner_name:/usr/share/test-reports/junit.xml ./junit.xml

if [ -z "$PUBLISH_STORYBOOK_PATH" ] || [ -z "$PUBLISH_STORYBOOK_BRANCH" ]; then
  echo "PUBLISH_STORYBOOK_PATH or PUBLISH_STORYBOOK_BRANCH not set"
else
  BRANCH_NAME_STRIPPED=${PUBLISH_STORYBOOK_BRANCH//[\/ ]/-}

  rm -rf dist/storybook
  mkdir -p dist/storybook
  docker cp $conatiner_name:/usr/share/nginx/html/ ./dist/storybook

  publishPath="$PUBLISH_STORYBOOK_PATH/$BRANCH_NAME_STRIPPED"

  rm -rf $publishPath
  mkdir -p $publishPath
  cp -r dist/storybook/html/* $publishPath

  html="<!DOCTYPE html><html lang=\"en\"><body>"

  for f in "$PUBLISH_STORYBOOK_PATH"/*/index.html
  do
    rel=${f#"${PUBLISH_STORYBOOK_PATH}"}
    rel2=${rel#"/"}

    text=${rel2%"/index.html"}

    html+="<p><a href=\"$rel2\">$text</a></p>"
  done

  html+="</body></html>"

  echo $html > $PUBLISH_STORYBOOK_PATH/index.html
fi

set -e
docker rm $conatiner_name

# docker cp verkar bevara modifieringsdatum på filen, och Bamboo gillar inte gamla cachade testrapporter
touch junit.xml
