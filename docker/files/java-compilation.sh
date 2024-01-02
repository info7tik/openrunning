#!/bin/bash

JAR_DIRECTORY="/root/jars"

rm -rf $JAR_DIRECTORY
mkdir $JAR_DIRECTORY

echo "Compile the model project"
pushd model
mvn clean install -DskipTest=True
popd

echo "Compile the gpxprocessor project"
pushd gpxprocessor
j2 *.j2 > src/main/resources/application.properties
mvn clean package -DskipTest=True
cp target/gpxprocessor-*.jar $JAR_DIRECTORY
popd

echo "Compile the backend project"
pushd backend
j2 *.j2 > src/main/resources/application.properties
mvn clean package -DskipTest=True
cp target/orbackend-*.jar $JAR_DIRECTORY
popd

