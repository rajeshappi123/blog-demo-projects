---
layout: post
title:  "Configure Shibboleth SP on macOS Sierra"
date:   2016-12-28 21:15:49 -0400
categories: jekyll update
---
##Software
 - [Shibboleth SP](https://shibboleth.net/products/service-provider.html) - Service Provider software to which allows to configure SSO-enabled web-sites/web-applications.
 - [Apache Web Server](https://httpd.apache.org/) - web server which will be used to expose our web sites and enable SSO for them. Shibboleth SP can integrate with either Apache or IIS - it cannot work standalone.
 - [MacPorts](https://www.macports.org/) - tool which allows installing different kind of Open Source software on you Mac. MacPorts is a legacy tool - [HomeBrew](http://brew.sh/) is a much better, easy to use, modern tool. Unfortunately, ShibbolethSP package right now (Dec 2016) is assembled for Mac Ports only.
 - [JDK keytool](http://docs.oracle.com/javadb/10.6.2.1/adminguide/cadminsslkeys.html) - utility which comes with Oracle JDK and allows creating private keys, self-signed, certs, public certs, key stores, etc. It can be considered as an alternative of [OpenSSL](https://www.openssl.org/).

##Installation steps

### 1. Install Shibboleth SP
- Download and install MacPorts from [https://www.macports.org/install.php](https://www.macports.org/install.php)
- Install Shibboleth SP using instructions from [NativeSPMacPortInstallation](https://wiki.shibboleth.net/confluence/display/SHIB2/NativeSPMacPortInstallation)

## 2. Create Private key and Public Cert
- Create self-signed certificate:
```
  keytool -genkey -keyalg RSA -alias local-dev -keystore local-dev.jks -validity 365 -keysize 2048
```
- Export public certificate:
```
  keytool -export -keystore local-dev.jks -alias local-dev -file local-dev.cer
```
- Export private key:
```
  keytool -importkeystore -srckeystore local-dev.jks -destkeystore local-dev.p12 -deststoretype PKCS12
```

As a result we got  a pare of public cert and private key which we can use for SSL on Apache.

## 3. Configure Apache

### 3.1 Configure Virtual Host

### 3.2 Configure SSL

### 3.3 Configure Shibboleth plugin/mod


## 4. Configure Shibboleth SP

{% include custom_footer.html %}
