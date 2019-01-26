---
layout: post
title:  "Maven, Azure DevOps and Azure Artifacts."
date:   2019-01-16 11:44:19 -0400
categories: devops ci
---
### Introduction
This post will describe how to setup Maven project in Azure DevOps CI, build and deploy its artifact to DevOps Artifacts.

#### Steps

##### 1. Setup project in GitHub
* Create a new Maven project:
  * Used Maven Quickstart Archetype: `mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4`
  * Added simple `Utils` class to make the project look like a library 
* [Source code at GitHub](https://github.com/mogikanen9/blog-demo-projects/tree/master/simple-lib) 


##### 2. Create Azure DevOps project
* Create a new project in Azure DevOps, ex: [https://dev.azure.com/mogikanensoftware/Maven%20Demos](https://dev.azure.com/mogikanensoftware/Maven%20Demos)

##### 3. Create Azure DevOps build pipeline
* Configure Azure DevOps CI pipeline:
  * Use "New pipeleine" in "Pipilenes -> Builds" in Azure DevOps project
  * Connect to GitHub and select your repo. You may want to install Azure DevOps GitHub app and grant access to your GitHub repo.
  * Select "Maven" in "Configure your pipeline" step.
  * Create new pipeline and push `azure-pipelines.yml` to your repo to keep it alogn with the source code.
  * Run build.
  * As the result you will get:
    1.  CI pipeline configued in Azure DevOps
    2. `azure-pipelines.yml` config in your repo, ex:
```
# Maven
# Build your Java project and run tests with Apache Maven.
# Add steps that analyze code, save build artifacts, deploy, and more:

trigger:
- master

pool:
  vmImage: 'Ubuntu-16.04'

steps:
- task: Maven@3
  inputs:
    mavenPomFile: 'simple-lib/pom.xml'
    mavenOptions: '-Xmx3072m'
    javaHomeOption: 'JDKVersion'
    jdkVersionOption: '1.11'
    jdkArchitectureOption: 'x64'
    publishJUnitResults: false
    testResultsFiles: '**/surefire-reports/TEST-*.xml'
    goals: 'package'
  ```

##### 4. Create feed in Artifacts
 * Azure DevOps Artifacts have a concept of feeds - they can be used for differnet purposes, have differnet access permissions, etc. Currently, the feeds support Maven, Gradle, NuGet, NPM, Python and Universal. It means they will act like the appropriate repositories - Maven repo in my example.
 * Use "Artifcacts -> New Feed" to create new feed: provide name, visibility/access and public source package settings. (I used "simple-demo-feed").
 * Use "Connect to feed" link to see conectivity option depending on your technology.
 * Setting for my "simple-demo-feed" Maven repo are below:
 ```
 <repository>
  <id>dev-azure-com-mogikanensoftware-simple-demo-feed</id>
  <url>https://pkgs.dev.azure.com/mogikanensoftware/_packaging/simple-demo-feed/maven/v1</url>
  <releases>
    <enabled>true</enabled>
  </releases>
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
</repository>
 ```

##### 5. Update CI pipeline to deploy to Artifacts
 
