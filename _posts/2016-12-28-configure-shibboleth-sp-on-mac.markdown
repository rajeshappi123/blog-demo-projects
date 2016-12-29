---
layout: post
title:  "Configure Shibboleth SP on macOS Sierra"
date:   2016-12-28 11:15:49 -0400
categories: apache shibboleth
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

  Apache2 on macOS configuration files are located at `/etc/apache2`. By default only http is available and default port is 80.

#### 3.1 Configure Virtual Host
 - Uncomment line `Include conf/extra/httpd-vhosts.conf` in `/etc/apache2/httpd.conf` (Apache config file)
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

- Create folder `sp.mogikanensoftware.com` for virtual host at `/Library/WebServer/Documents/`;
- Create directory `secure` inside folder `/Library/WebServer/Documents/sp.mogikanensoftware.com`. It will be the secured zone;
- Place one index.html inside both root folder of virtual host and `secure` folder.

#### 3.2 Configure SSL
 Will use previously created private and public key for SSL.
- Copy files `local-dev.cer` and `local-dev.p12` to folder `/etc/apache2`
- Rename public cert file `local-dev.cer` to `server.crt`;
- Rename private key file `local-dev.p12` to `server.key`;
- Add/or uncomment 2 Apache plugins/mods in `/etc/apache2/httpd.conf`:

~~~~
LoadModule socache_shmcb_module libexec/apache2/mod_socache_shmcb.so
LoadModule ssl_module libexec/apache2/mod_ssl.so
~~~~

- Uncomment line `Include /private/etc/apache2/extra/httpd-ssl.conf` in `/etc/apache2/httpd.conf`
- Update `httpd-ssl.conf` config to configure SSL for the virtual host:

~~~~
DocumentRoot "/Library/WebServer/Documents/sp.mogikanensoftware.com"
ServerName sp.mogikanensoftware.com:443
~~~~

- Please, verify and confirm that the following lines are uncommented and point to the correct files (our key and cert):

~~~~
SSLCertificateFile "/private/etc/apache2/server.crt"
SLCertificateKeyFile "/private/etc/apache2/server.key"
~~~~

#### 3.3 Configure Shibboleth plugin/mod
- Create folder `shibboleth-conf` inside `/private/etc/apache2/extra/`;
- Locate Shibboleth SP installed folder: `/opt/local/etc/shibboleth`;
- Copy `apache24.config` file from Shibboleth SP folder to folder `/private/etc/apache2/extra/shibboleth-conf`;
- Enable Shibboleth SP and Apache integration:
 * Modify file `/etc/apache2/httpd.conf` - add line:
~~~~
  Include /private/etc/apache2/extra/shibboleth-conf/apache24.config
~~~~

  * Enable Shibboleth SP plugin/mod by adding the following line to `apache24.config`:

~~~~
  LoadModule mod_shib /opt/local/lib/shibboleth/mod_shib_24.so
~~~~

***
### 4. Configure Shibboleth SP
It's not a simple task to fully properly configure Shibboleth SP. A very basic example of such configuration is below:
- Add/configure host in Request Mapper section:

~~~~
<RequestMapper type="Native">
  <RequestMap>
    <Host name="sp.mogikanensoftware.com" scheme="http">
      <Path name="secure" authType="shibboleth" requireSession="true"
        forceAuthn="true" />
    </Host>
  </RequestMap>
</RequestMapper>
~~~~

- Configure/modify Application Defaults to use our SP:

~~~~
  <ApplicationDefaults entityID="https://sp.mogikanensoftware.com/Shibboleth.sso"
		REMOTE_USER="eppn persistent-id targeted-id">
~~~~

- Configure the appropriate Identity Provider:

  * Specify IdP/SSO entityID:

~~~~
  <SSO entityID="http://eumadfsdev.extranetusermanager.com/adfs/services/trust">
    SAML2 SAML1
  </SSO>
~~~~

  * Configure IdP/SSO metadata:

~~~~
  <MetadataProvider type="XML" file="FederationMetadata.xml">
  </MetadataProvider>
~~~~

- Configure `CredentialResolver` using private key and public certs created using `keytool` in step #2 at the very beginning
~~~~
  <!-- Simple file-based resolver for using a single keypair. -->
  <CredentialResolver type="File" key="omd-local-dev.p12" certificate="omd-local-dev.cer"/>
~~~~
- Start/or restart Apache:
~~~~
  ./apachectl start
~~~~
  or
~~~~
./apachectl restart
~~~~

- Start Shibboleth SP daemon:
~~~~
  launchctl load -F /Library/LaunchDaemons/org.macports.shibd.plist
~~~~
- Generate your SP Metadata file by hitting [http://sp.mogikanensoftware.com/Shibboleth.sso/Metadata](http://sp.mogikanensoftware.com/Shibboleth.sso/Metadata)

- Share your SP Metadata file with your Identity Provider

***
### 5. Final steps

- Confirm that the Identity Provider (SSO) your SP points to has successfully imported your SP Metadata;
- Open public content of your virtual sites - [https://sp.mogikanensoftware.com/index.html](https://sp.mogikanensoftware.com/index.html). You should get the page without any security handling;
- Open/try to access secured page - [https://sp.mogikanensoftware.com/secure/index.html](https://sp.mogikanensoftware.com/secure/index.html). You should be redirected to your Identity Provider Authentication process (login page);
- Authenticate at your IdP. You will be redirected to your secured page;
- Congratulations! We are done!

***
### Additional notes
 - The above  describe a simplified way of configuration;
 - In reality there might be multiple virtual sites,  multiple Identity Providers, etc.
 - It's highly recommended to read the official [Shibboleth SP documentation](https://wiki.shibboleth.net/confluence/display/SHIB2)

{% include custom_footer.html %}
