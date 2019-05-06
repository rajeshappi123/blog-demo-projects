# Download base image ubuntu18.04
FROM ubuntu:18.04

# Add Maintainer Info
LABEL maintainer="mogikanensoftware@gmail.com"

# Update Ubuntu Software repository
RUN apt-get update

# Install Open JDK
RUN apt-get install -y openjdk-8-jdk

# Install Git (Default version - 2.7)
RUN apt-get install -y git

# Install Ansible (Default version - 2.7)
RUN apt-get install -y software-properties-common
RUN apt-add-repository -y -u ppa:ansible/ansible
RUN apt-get install -y ansible

#Install Node (Default version - 8.10)
RUN apt-get install -y nodejs && apt-get install -y npm

#Install Maven (Default version - 3.6)
RUN apt-get install -y maven