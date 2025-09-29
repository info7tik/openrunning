ARG REPOSITORY_DIR="/build/m2"
ARG MVN_DOWNLOAD_ONLY="-Dmaven.repo.local=$REPOSITORY_DIR -Dmaven.main.skip -Dmaven.test.skip"
ARG MVN_COMPILE="-Dmaven.repo.local=$REPOSITORY_DIR -Dmaven.test.skip"

### Build the model library
FROM maven:3.8.5-openjdk-17 AS modelbuilder

# Import variables to the stage
ARG MVN_DOWNLOAD_ONLY
ARG MVN_COMPILE

WORKDIR /build

# Copy the pom.xml to download all maven dependencies
COPY model/pom.xml pom.xml
RUN mvn clean package $MVN_DOWNLOAD_ONLY
# Build the application to another Docker layers
COPY model/src src
RUN mvn clean install $MVN_COMPILE

### Build the GPX file processor
FROM maven:3.8.5-openjdk-17 AS processor-builder

# Import variables to the stage
ARG REPOSITORY_DIR
ARG MVN_DOWNLOAD_ONLY
ARG MVN_COMPILE

WORKDIR /build

# Import the model library
COPY --from=modelbuilder $REPOSITORY_DIR $REPOSITORY_DIR

# Copy the pom.xml to download all maven dependencies
COPY gpxprocessor/pom.xml pom.xml
RUN mvn clean package $MVN_DOWNLOAD_ONLY

# Build the application to another Docker layers
COPY gpxprocessor/src src
RUN mvn clean package $MVN_COMPILE

### Build the final processor container
FROM openjdk:17-slim-buster
# Set the current directory
WORKDIR /root
# Copy the applications to the container
COPY --from=processor-builder /build/target/gpxprocessor-*.jar .
# Start the GPX processor
ENTRYPOINT [ "bash", "-c", "java -jar gpxprocessor-*.jar" ]
