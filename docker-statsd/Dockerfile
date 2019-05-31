# Download base image ubuntu18.04
FROM ubuntu:16.04

# Add Maintainer Info
LABEL maintainer="mogikanensoftware@gmail.com"

# Update Ubuntu Software repository
RUN apt-get -y update

# Install StatsD dependencied
RUN apt-get install -y git nodejs npm devscripts debhelper dh-systemd

# Checkout statsd sources
RUN cd /tmp && git clone https://github.com/etsy/statsd.git

# Build statsd sources
RUN cd /tmp/statsd && dpkg-buildpackage 

# Install statsd
RUN cd /tmp && dpkg -i statsd_0.8.0-1_all.deb
