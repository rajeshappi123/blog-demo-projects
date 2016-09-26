---
layout: post
title:  "My First Steps with HAPI. Pat 1. Introduction."
date:   2016-09-25 22:55:49 -0400
categories: java hapi
---
This post is an introduction into HAPI library.

### Introduction

We are starting a new project in Healthcare integration domain. The core functionality can be described like "a service/application which accepts HL7v2 messages over HTTP, validates them, transforms into custom XML and delivers to the recipients via SFTP". When I googled for "HL7v2 Java" - HAPI is the first thing which tops the results. What does it mean? Let's start with some definitions:
 -  [HL7](https://en.wikipedia.org/wiki/Health_Level_7) stands for "Health Level 7". It's a standard which describes how to transfer different healthcare clinical data.
 -  [HL7v2](https://en.wikipedia.org/wiki/Health_Level_7#HL7_version_2) is a version of HL7 standard developed in 1989 and it is based on pipes `|` and caret `^`.
 -  [HAPI](http://hl7api.sourceforge.net/) is a Java-based Hl7v2 parser library.

When I first heard about Hl7v2 several years ago and reviewed HL7v2 messages it looked like a bad joke. Neither XML nor JSON, really? Well, 1989 - what else you can expect. On the other side the protocol is widely adopted and currently it is still the main workhorse in healthcare industry.

First results of checking HAPI website:
- The latest stable version is 2.2 and the sast published date is "2014-05-12";
- The site is hosted at [sourceforge.net](https://sourceforge.net/);
- The library is built by [Maven](http://maven.apache.org/);
- Lots of examples in [Developing using HAPI - By Example](http://hl7api.sourceforge.net/devbyexample.html) section;
- Decently looking Documentation section with references to all HL7v2.1 - HL7v2.6 messages.

First impression conclusion - legacy library for legacy protocol with its pros. and cons.

Pros.
- Open source;
- Should be stable and well tested;
- Decent documentation:
- Decent community;
- Maven based.

Cons:
- No Java 8 support;
- Latest Spring Framework support is questionable;
- Support in general is a grey area.

Well, let's take next step.

Looked really promising.
