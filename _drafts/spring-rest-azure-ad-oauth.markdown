---
layout: post
title:  "Secure Spring Boot REST API with OAuth 2.0 Client Credentials Flow using Azure AD."
date:   2020-04-26 10:00:01 -0400
categories: java spring rest azuread oauth2
---
### Introduction
The following post will describe how to secure Spring Boot REST API with OAuth2 2.0 Client Credentials Flow (M2M) using Azure AD as Authorization Server. The focus will be on Azure AD setup and realted Spring Boot/Spring Security configuration nuances.

### Brief and high level overview

OAuth2 2.0 Client Credentials Flow (M2M) is intended to cover Machine-to-Machine (M2M) authentication when a human interction is not available or applicable (ex: a scheduled job calls a secured api). The flow includes 3 parties ( `Auhorization Server`, `Resource Server` and `Client`) and contains the following major steps:

1. `Client` sends authoriation request to Authorization Server to get an access token. Client is using `Client ID` and `Client Secret` (as credentials) and provides `Scope` of the request. `Scope` identifies the resource/access the client is trying to get.

2. `Authorization Server` authenticates the `Client` and provides back an `access token`. 

3. `Client` calls the `Resource Server` and provides `access token` as a part of the request.

4. `Resource Server` verifies `access token` and  provides access to the requested resource.

<br/>

### Our Example/Approach

#### 1. Authorization Server

Will be using [Microsoft Azure Active Directory](https://azure.microsoft.com/en-ca/services/active-directory/) (Azure AD) as Authorization Server. Azure AD supports OAuth2 2.0 Client Credentials Flow and provides all the neccessary configruation options.

#### 2. Resource Server
The exmaple will have a [Spring Boot](https://spring.io/projects/spring-boot) based REST API with 2 endpoints. Will be using [Spring Security OAuth 2.0 Resource Server](https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/html/boot-features-security-oauth2-resource-server.html) to protect the API and integrate with the Authorization Server.


#### 3. Client
Will be using [Curl](https://curl.haxx.se/) as our HTTP cleint to demonstrate that our approach is pure HTTP based, complient with OAuth 2.0 and client technology agnostic.

<br/>

### Steps

<br/>

#### 1. Authorization Server - Azure AD Setup



<br/>

#### 2. Resource Server - REST API


<br/>

#### 3. Client 


<br/>

### References:
* [Azure AD and the Oauth 2.0 Client Credentials Flow](https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-client-creds-grant-flow)
* [Spring Security: Configure Authorization)](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2resourceserver-jwt-authorization)
* [Demo project source code](https://github.com/mogikanen9/blog-demo-projects/edit/master/simple-lib/.gitignore)
 
 {% include custom_footer.html %}