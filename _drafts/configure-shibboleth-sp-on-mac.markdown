---
layout: post
title:  "Configure Shibboleth SP on macOS Sierra"
date:   2016-12-28 11:15:49 -0400
categories: jekyll update
---

## Software
 - [Shibboleth SP](https://shibboleth.net/products/service-provider.html) - Service Provider software to which allows to configure SSO-enabled web-sites/web-applications.
 - [Apache Web Server](https://httpd.apache.org/) - web server which will be used to expose our web sites and enable SSO for them. Shibboleth SP can integrate with either Apache or IIS - it cannot work standalone.
 - [MacPorts](https://www.macports.org/) - tool which allows installing different kind of Open Source software on you Mac. MacPorts is a legacy tool - [HomeBrew](http://brew.sh/) is a much better, easy to use, modern tool. Unfortunately, ShibbolethSP package right now (Dec 2016) is assembled for Mac Ports only.
 - [JDK keytool](http://docs.oracle.com/javadb/10.6.2.1/adminguide/cadminsslkeys.html) - utility which comes with Oracle JDK and allows creating private keys, self-signed, certs, public certs, key stores, etc. It can be considered as an alternative of [OpenSSL](https://www.openssl.org/).


## Installation steps

### 1. Install Shibboleth SP
- Download and install MacPorts from [https://www.macports.org/install.php](https://www.macports.org/install.php)
- Install Shibboleth SP using instructions from [NativeSPMacPortInstallation](https://wiki.shibboleth.net/confluence/display/SHIB2/NativeSPMacPortInstallation)

***
### 2. Create Private key and Public Cert
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

***
### 3. Configure Apache

  Apache2 on macOS configuration files are located at /etc/apache2. By default only http is available and default port is 80.


#### 3.1 Configure Virtual Host
 - Uncomment line 'Include conf/extra/httpd-vhosts.conf' in '/etc/apache2/httpd.conf' (Apache config file)
 - Create virtual host by updating httpd-vhosts.conf config file:

 ```

 <VirtualHost *:80>
    ServerAdmin webmaster@sp.mogikanensoftware.com
    DocumentRoot "/Library/WebServer/Documents/sp.mogikanensoftware.com"
    ServerName sp.mogikanensoftware.com
    ServerAlias www.sp.mogikanensoftware.com
    ErrorLog "/private/var/log/apache2/sp.mogikanensoftware.com-error_log"
    CustomLog "/private/var/log/apache2/sp.mogikanensoftware.com-access_log" common

</VirtualHost>

 ```

- Create folder 'sp.mogikanensoftware.com' for virtual host at '/Library/WebServer/Documents/';
- Create directory 'secure' inside folder '/Library/WebServer/Documents/sp.mogikanensoftware.com'. It will be the secured zone;
- Place one index.html inside both root folder of virtual host and 'secure' folder.

#### 3.2 Configure SSL
 Will use previsouly created private and public key for SSL.
- Copy files 'local-dev.cer' and 'local-dev.p12' to folder '/etc/apache2'
- Rename public cert file 'local-dev.cer' to 'server.crt';
- Rename private key file 'ocal-dev.p12' to 'server.key';
- Add/or uncomment 2 Apache plugins/mods in '/etc/apache2/httpd.conf':

 ```
  LoadModule socache_shmcb_module libexec/apache2/mod_socache_shmcb.so

  LoadModule ssl_module libexec/apache2/mod_ssl.so
  ```

- Uncomment line 'Include /private/etc/apache2/extra/httpd-ssl.conf' in '/etc/apache2/httpd.conf'
- Update 'httpd-ssl.conf' config to configure SSL for the vistual host:

```
#   General setup for the virtual host
DocumentRoot "/Library/WebServer/Documents/sp.mogikanensoftware.com"
ServerName sp.mogikanensoftware.com:443
```

- Please, verify and confirm that the follwoing lines are uncommneted and poin to the correct files (our key and cert):

```
  SSLCertificateFile "/private/etc/apache2/server.crt"
  ....................
  SLCertificateKeyFile "/private/etc/apache2/server.key"
```

#### 3.3 Configure Shibboleth plugin/mod

***
### 4. Configure Shibboleth SP

{% include custom_footer.html %}
