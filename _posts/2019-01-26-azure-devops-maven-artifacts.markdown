---
layout: post
title:  "Maven, Azure DevOps Pipelines and Artifacts."
date:   2019-01-26 11:44:19 -0400
categories: devops ci
---
### Introduction
This post will describe how to setup Maven project in Azure DevOps CI, build and deploy its artifact to DevOps Artifacts.

<br/>
### Steps

<br/>
#### 1. Setup project in GitHub
Create a new Maven project using Maven Quickstart Archetype:

{% highlight bash %}

mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4

{% endhighlight %}

<br/>
#### 2. Create Azure DevOps project
Create a new project in Azure DevOps, ex: [https://dev.azure.com/mogikanensoftware/Maven%20Demos](https://dev.azure.com/mogikanensoftware/Maven%20Demos)

<br/>
#### 3. Create Azure DevOps build pipeline
Configure Azure DevOps CI pipeline:

* Use "New pipeleine" in "Pipilenes -> Builds" in Azure DevOps project.
* Connect to GitHub and select your repo. You may want to install Azure DevOps GitHub app and grant access to your GitHub repo.
* Select "Maven" in "Configure your pipeline" step.
* Create new pipeline and push `azure-pipelines.yml` to your repo to keep it alogn with the source code and run build.
* As the result you will get:
  1.  CI pipeline configued in Azure DevOps
  2. `azure-pipelines.yml` config in your repo, ex:

{% highlight bash %}

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

{% endhighlight %}

<br/>
#### 4. Create feed in Artifacts
* Azure DevOps Artifacts have a concept of feeds - they can be used for differnet purposes, have differnet access permissions, etc. Currently, the feeds support Maven, Gradle, NuGet, NPM, Python and Universal. It means they will act like the appropriate repositories - Maven repo in my example.
* Use "Artifcacts -> New Feed" to create new feed: provide name, visibility/access and public source package settings. (I used "simple-demo-feed").
* Use "Connect to feed" link to see conectivity option depending on your technology.
* Setting for my "simple-demo-feed" Maven repo are below:

{% highlight bash %}

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

{% endhighlight %}

<br/>
#### 5. Update CI pipeline to deploy to Artifacts
* In order to access `simple-demo-feed` Maven repo from other projects on your computer you need to generate Maven credentials for this repo by using "COnnect to feed -> Maven -> Generate Maven credentials" option. The credentails will be valid for 90 days. Currently there is no way to change this settings.
* No need to use credentials in your CI pipeline since Azure DevOps environment automatically grants access for feeds inside the environment - there is one catch though: `mavenAuthenticateFeed: true` option must be setup in Maven Task of `azure-piplenies.yml`, ex:

{% highlight bash %}

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
    mavenAuthenticateFeed: true
    publishJUnitResults: false
    testResultsFiles: '**/surefire-reports/TEST-*.xml'
    goals: 'deploy'

{% endhighlight %}

As the result upon successfull build run an the artifact appears in feed and is available for reference in other Maven and/or Gradle projects as standard Maven repo.

References:
* [Set up Azure Pipelines and Maven)](https://docs.microsoft.com/en-us/azure/devops/pipelines/artifacts/maven?toc=%2Fazure%2Fdevops%2Fartifacts%2Ftoc.json&view=azdevops#)
* [Azure DevOps Artifacts](https://docs.microsoft.com/en-us/azure/devops/artifacts/)
* [Demo project source code](https://github.com/mogikanen9/blog-demo-projects/edit/master/simple-lib/.gitignore)
 
 {% include custom_footer.html %}