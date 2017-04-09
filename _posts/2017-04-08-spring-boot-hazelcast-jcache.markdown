---
layout: post
title:  "Spring Boot, JCache and Hazelcast!"
date:   2017-04-08 19:05:09 -0400
categories: java spring jcache hazelcast
---
### Introduction

This post is about using JCache API/Hazelcast implementation in Spring Boot application.

This exercise is based on the sample from Hazelcast team available at
[GitHub Hazelcast Code Samples Repository](https://github.com/hazelcast/hazelcast-code-samples/tree/master/hazelcast-integration/springboot-caching-jcache).  

Will use [Maven](https://maven.apache.org/) as our build and dependency management tool.

### Definitions

 -  [JCache (JSR 107)](https://www.jcp.org/en/jsr/detail?id=107) is a common API for
 using caching in Java.
 -  [Hazelcast](https://hazelcast.org/) is an open source In-Memory Data Grid
 product. It is also a full implementation of JCache(JSR 107).
 -  [Spring Boot](https://projects.spring.io/spring-boot/) is an opionated version
 of Spring Framework which combines many other Spring projects.

### Steps

#### 1. Configure your Spring Boot project.

#### 2. Add caching starter to your Spring Boot project:
{% highlight xml %}
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
{% endhighlight %}

#### 3.  Add JCache API to your classpath:
{% highlight xml %}
<dependency>
    <groupId>javax.cache</groupId>
    <artifactId>cache-api</artifactId>
</dependency>
{% endhighlight %}

#### 4.  Add Hazelcast to the classpath:
{% highlight xml %}
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast-all</artifactId>
    <version>3.7.4</version>
</dependency>
{% endhighlight %}

#### 5.  Specify cache type and jcache provider in application.properties configuration file:
{% highlight properties %}
spring.cache.type=jcache
spring.cache.jcache.provider=com.hazelcast.cache.impl.HazelcastServerCachingProvider
{% endhighlight %}

#### 6.  Add hazelcast.xml to the classpath and define your cache there, ex.:
{% highlight xml %}
<hazelcast
	xsi:schemaLocation="http://www.hazelcast.com/schema/config hazelcast-config-3.7.xsd"
	xmlns="http://www.hazelcast.com/schema/config" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <cache name="events">
        <management-enabled>true</management-enabled>
    </cache>
</hazelcast>
{% endhighlight %}

#### 7.  Add `@EnableCaching` annotation to your configuration class.

#### 8.  Disable auto configuration for Hazelcast in your Spring Boot application configuration class:
{% highlight java %}  
@SpringBootApplication
@EnableAutoConfiguration(exclude = {HazelcastAutoConfiguration.class})
@EnableCaching
public class SpringBootHazelcastJCacheApplication {
  public static void main(String[] args) {
		SpringApplication.run(SpringBootHazelcastJCacheApplication.class, args);
	}
}
{% endhighlight %}

#### 9.  Use JCache(JSR107) annotation to annotate your cache targets, ex:
{% highlight java %}
public interface EventService {
	@CacheResult(cacheName="events")
	Event getById(String eventId);
}
{% endhighlight %}

#### 10.  Enjoy caching!

### Details and nuances
If you add Hazelcast to the classpath of Spring Boot project, then Spring Boot [automatically configures one Hazelcast instance](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-hazelcast.html) in its context. We don't need this instance - we need only JCache manager and provider to be initialized following standard JCache approach. That's why we need step #8 - other wise will endup havenig 2 Hazelcast instances in our application:
{% highlight log %}
Members [2] {
	Member [192.168.1.137]:5701 - db7634e1-8660-42c4-881c-eff7af1d73b8
	Member [192.168.1.137]:5702 - 256d96f2-b198-4ddf-b89e-fef9c4fd9f6d this
}
{% endhighlight %}

{% include custom_footer.html %}
