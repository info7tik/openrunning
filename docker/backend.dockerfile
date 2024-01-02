FROM openjdk:17-slim-buster

# Set the current directory
WORKDIR /root
# Copy the applications to the container
ADD dist/gpxprocessor-*.jar .
ADD dist/orbackend-*.jar .
# Start the backend API
CMD ["bash"]
ENTRYPOINT [ "bash", "-c", "java -jar orbackend-*.jar" ]
