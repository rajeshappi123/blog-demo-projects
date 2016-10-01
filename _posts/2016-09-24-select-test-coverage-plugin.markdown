---
layout: post
title:  "Cobertura and JaCoCo: using test coverage plugin"
date:   2016-09-23 21:15:49 -0400
categories: jekyll update
---
Recently I decided to configure a test coverage plugin for one of my pet projects to have a better visibility of my unit tests. At work we use [SonarQube](http://www.sonarqube.org/) - too much for my purpose. I immediately looked at Cobertura and its [Maven plugin](http://www.mojohaus.org/cobertura-maven-plugin/). Several years ago I setup Cobertura for couple of real projects at our CI server and it worked great. The setup was straightforward and I started to enjoy my test coverage report. My happy time was short - once I started to use lambdas (introduced in Java 8) extensively the plugin started to struggle with analysis. The plugin failed to check those classes and generated a errors like:

```
java.lang.IllegalArgumentException: INVOKESPECIAL/STATIC on interfaces require ASM 5
```

I was using version 2.7 which supposed to support Java 8. Once googled I found that I am not the only one who is facing those issues. People reported same issues at their [GitHub page](https://github.com/cobertura/cobertura/issues/166)

Dilemma: should I spent more time digging into it or try an alternative?

I knew that SonarQube was using [JaCoCo](http://www.eclemma.org/jacoco/) and I also used Emma Eclipse plugin long time ago and it also worked quite well. Decided to try JaCoco. The setup was not as neat as for Cobertura plugin but I made it working and it worked perfectly with Java 8.

The final Maven plugin configuration is below.

```
<build>
	...
  <plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.7.7.201606060606</version>
    <configuration>
      <destFile>${basedir}/target/coverage-reports/jacoco-unit.exec</destFile>
      <dataFile>${basedir}/target/coverage-reports/jacoco-unit.exec</dataFile>
      </configuration>
        <executions>
          <execution>
            <id>jacoco-initialize</id>
            <goals>
              <goal>prepare-agent</goal>
            </goals>
          </execution>
          <execution>
            <id>jacoco-site</id>
            <phase>package</phase>
            <goals>
              <goal>report</goal>
              </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
    ...
</build>
```
{% include custom_footer.html %}
