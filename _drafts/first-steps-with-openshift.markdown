---
layout: post
title:  "My First Steps with OpenShift. Pat 1. Introduction."
date:   2016-09-24 21:15:49 -0400
categories: jekyll update
---
[OpenShift](https://www.openshift.com/) is a PaaS built by [RedHat](https://www.redhat.com/) on with Docker and Kubernetes. What does all that mean? Let's start with some definitions:
- PaaS is ["Platform as a Service"](https://en.wikipedia.org/wiki/Platform_as_a_service), a category of cloud services which allows developer to build, run and deploy applications without spending too much time on maintaining infrastructure.
- [Docker](https://www.docker.com/) is a "containerization platform" - thing which allows you to deploy your apps inside a container and not inside a VM.
- [Kubernetes](http://kubernetes.io/) is tool which allows you to manage your containers and automate your deployment.

What are you getting with their free account?

- You will be able to deploy three applications since they give you three small cartridges (a cartridge is an application runtime environment);
- They have a decent variety of cartridges which provide runtime for different technologies such as Java, Ruby, Python, PHP, NodeJS.
-	There are cartridges to support your Continuous Integration process, ex. Jenkins cartridge;
- They even provide you with a DIS cartridge, so you can build your own.

I was looking to a possibility to deploy my Spring-base service app. So everything looked very promising.The only thing which bothered me that the latest version of Tomcat which was available in the list with Java cartridges was Tomcat 7. Not a deal breaker, especially since I got everything for free.
