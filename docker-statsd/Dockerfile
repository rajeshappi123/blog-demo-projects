# Download base image ubuntu18.04
FROM ubuntu:16.04

# Add Maintainer Info
LABEL maintainer="mogikanensoftware@gmail.com"

# Update Ubuntu Software repository
RUN apt-get -y update

RUN apt-get install -y curl && curl -sL https://deb.nodesource.com/setup_8.x | bash -

# Install StatsD dependencied
RUN apt-get install -y git nodejs

# Checkout statsd sources
RUN cd /tmp && git clone https://github.com/etsy/statsd.git

COPY ./myConfig.js /tmp/statsd

# Start statsd from sources
ENTRYPOINT ["node","/tmp/statsd/stats.js","/tmp/statsd/myConfig.js"]
