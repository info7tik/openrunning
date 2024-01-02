FROM maven:3.8-openjdk-17-slim

RUN apt update && apt -y install j2cli
# Set the current directory
WORKDIR root
# Copy the Java sources to the container
ADD model model
ADD gpxprocessor gpxprocessor
ADD backend backend
# Copy the compilation script to the container
ADD docker/files/java-compilation.sh .
# Copy the configuration files of the Java applications
ADD docker/files/gpxprocessor.properties.j2 gpxprocessor/
ADD docker/files/backend.properties.j2 backend/

ENTRYPOINT [ "bash", "java-compilation.sh" ]
