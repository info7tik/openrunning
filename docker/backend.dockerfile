FROM openjdk:17-slim-buster

# Set the current directory
WORKDIR /root
# Copy the applications to the container
ADD gpxprocessor/target/gpxprocessor-*.jar .
ADD backend/target/orbackend-*.jar .
# Start the backend API
CMD ["bash"]
ENTRYPOINT [ "bash", "-c", "java -jar orbackend-*.jar" ]
