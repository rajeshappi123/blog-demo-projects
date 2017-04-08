---
layout: post
title:  "Spring Boot, JCache and Hazelcast!"
date:   2017-04-08 19:05:09 -0400
categories: java spring jcache hazelcast
---
This post is about using JCache API/Hazelcast implementation in Spring Boot application.

### Introduction

Let's start with some definitions:
 -  [JCache (JSR 107)](https://www.jcp.org/en/jsr/detail?id=107) is a common API for
 using caching in Java.
 -  [Hazelcast](https://hazelcast.org/) is an open source In-Memory Data Grid
 product. It is also a full implementation of JCache(JSR 107).
 -  [Spring Boot](https://projects.spring.io/spring-boot/) is an opionated version
 of Spring Framework which combines many other Spring projects.

### Details and nuances
This exercize is based on the sample from Hazelcast folks available at
[GitHub Hazelcast Code Samples Repository](https://github.com/hazelcast/hazelcast-code-samples/tree/master/hazelcast-integration/springboot-caching-jcache)

{% include custom_footer.html %}
