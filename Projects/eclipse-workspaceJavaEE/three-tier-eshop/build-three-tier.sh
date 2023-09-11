#!/bin/bash
 if [ "$#" -eq  "0" ]
   then
     echo "Applikationsnamn måste anges"
 else

mvn archetype:generate \
  -DarchetypeGroupId=org.apache.maven.archetypes \
  -DarchetypeArtifactId=maven-archetype-webapp \
  -DarchetypeVersion=1.4 \
  -DinteractiveMode=false \
  -DgroupId=com.eshop \
  -DartifactId=$1 \
  -Dversion=1.0-SNAPSHOT

cd $1
mkdir -p \
  src/main/java/com/eshop/ft/beans \
  src/main/java/com/eshop/ft/controllers \
  src/main/java/com/eshop/bt/vo \
  src/main/java/com/eshop/bt/ejb \
  src/main/java/com/eshop/bt/to \
  src/main/java/com/eshop/dt/client \
  src/main/java/com/eshop/dt/eo \
  src/main/java/com/eshop/dt/dao \
  src/main/resources \
  src/test/java/com/eshop/ft/beans \
  src/test/java/com/eshop/ft/controllers \
  src/test/java/com/eshop/bt/vo \
  src/test/java/com/eshop/bt/ejb \
  src/test/java/com/eshop/bt/to \
  src/test/java/com/eshop/dt/client \
  src/test/java/com/eshop/dt/eo \
  src/test/java/com/eshop/dt/dao \
  src/test/resources
sed -i.bak 's/1.7/1.8/g'  pom.xml
rm pom.xml.bak
fi
