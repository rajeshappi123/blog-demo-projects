####
# This Dockerfile is used in order to build a container that runs the Quarkus application in JVM mode
#
# Before building the docker image run:
#
# mvn package
#
# Then, build the image with:
#
# docker build -f src/main/docker/Dockerfile.jvm -t quarkus/rest-client-jvm .
#
# Then run the container using:
#
# docker run -i --rm -p 8080:8080 quarkus/rest-client-jvm
#
###
# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
MAINTAINER Vlad Voinov <mogikanensoftware@gmail.com>

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8082
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/rest-client-1.0-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} rest-client-1.0-SNAPSHOT.jar

# Run the jar file 
ENTRYPOINT ["java","-jar","/rest-client-1.0-SNAPSHOT.jar"]