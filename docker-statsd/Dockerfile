# Download base image ubuntu18.04
FROM ubuntu:16.04

# Add Maintainer Info
LABEL maintainer="mogikanensoftware@gmail.com"

# Update Ubuntu Software repository
RUN apt-get -y update

# Install Open JDK
RUN apt-get install -y openjdk-8-jdk

# Install StatsD dependencied
RUN apt-get install -y git nodejs npm devscripts debhelper dh-systemd
