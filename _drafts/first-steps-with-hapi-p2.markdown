---
layout: post
title:  "My First Steps with HAPI. Part 2. Parsing HL7 message."
date:   2016-10-01 17:55:49 -0400
categories: java hapi
---
This post is a second part of my introduction into HAPI library.

### Parse HL7 message

We will start with a simple project which will demonstrate how to parse HL7 messages using HAPI API.

#### Tools and libraries will use
- JDK 1.8
- Maven
- Eclipse (STS 3.8.1)
- HAPI API v2.2

HAPI has a very good [set of examples](http://hl7api.sourceforge.net/devbyexample.html)
which is a good starting point. Will use it as well - will jump right to section 'Parsing Messages'.

#### Configuration steps
- Create empty Maven based project simple-hl7-parser using basic archetype;
- Add HL7 dependencies:
```
..........................
  <properties>
		<hapi.version>2.2</hapi.version>
	</properties>
  <dependency>
    <groupId>ca.uhn.hapi</groupId>
    <artifactId>hapi-base</artifactId>
    <version>${hapi.version}</version>
    <scope>compile</scope>
  </dependency>
  <dependency>
    <groupId>ca.uhn.hapi</groupId>
    <artifactId>hapi-structures-v24</artifactId>
    <version>${hapi.version}</version>
  </dependency>
..........................
```
- Import project into the IDE.

*Notes: HAPI library packs structures for different HL7 versions in different artifacts. In my case I will use/parse HL7v2.4 messages -  I only included 'hapi-structures-v24' artifact.*


{% include custom_footer.html %}
