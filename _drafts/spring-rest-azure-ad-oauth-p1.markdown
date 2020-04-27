---
layout: post
title:  "Secure Spring Boot REST API with OAuth 2.0 Client Credentials Flow using Azure AD. Part 1."
date:   2020-04-26 10:00:01 -0400
categories: java spring rest azuread oauth2
---
### Introduction
The following post will describe how to secure Spring Boot REST API with OAuth2 2.0 Client Credentials Flow (M2M) using Azure AD as Authorization Server. The focus will be on Azure AD setup and realted Spring Boot/Spring Security configuration nuances. The post will be devided into 2 parts: `Part 1. Overview and Azure AD setup` and `Part 2. Spring REST API configuration`.

## Part 1. Overview and Azure AD setup

### Overview

OAuth2 2.0 Client Credentials Flow (M2M) is intended to cover Machine-to-Machine (M2M) authentication when a human interction is not available or applicable (ex: a scheduled job calls a secured api). The flow includes 3 parties ( `Auhorization Server`, `Resource Server` and `Client`) and contains the following major steps:

1. `Client` sends authoriation request to Authorization Server to get an access token. Client is using `Client ID` and `Client Secret` (as credentials) and provides `Scope` of the request. `Scope` identifies the resource/access the client is trying to get.

2. `Authorization Server` authenticates the `Client` and provides back an `access token`. 

3. `Client` calls the `Resource Server` and provides `access token` as a part of the request.

4. `Resource Server` verifies `access token` and  provides access to the requested resource.

<br/>

### This Example/Approach

<br/>

#### 1. Authorization Server

Will be using [Microsoft Azure Active Directory](https://azure.microsoft.com/en-ca/services/active-directory/) (Azure AD) as Authorization Server. Azure AD supports OAuth2 2.0 Client Credentials Flow and provides all the neccessary configruation options.

<br/>

#### 2. Resource Server
The exmaple will have a [Spring Boot](https://spring.io/projects/spring-boot) based REST API with 2 endpoints. Will be using [Spring Security OAuth 2.0 Resource Server](https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/html/boot-features-security-oauth2-resource-server.html) to protect the API and integrate with the Authorization Server.


#### 3. Client
Will be using [Curl](https://curl.haxx.se/) as our HTTP cleint to demonstrate that our approach is pure HTTP based, complient with OAuth 2.0 and client technology agnostic.

<br/>

### Azure AD Setup

<br/>

#### API Registration

1. Create API (Resource Server) registration in Azure AD by following steps in [https://docs.microsoft.com/en-us/azure/active-directory/develop/quickstart-register-app](https://docs.microsoft.com/en-us/azure/active-directory/develop/quickstart-register-app). 

Please, note that step with `Redirect URI` is optional - no need to provide anything there.

2. Setup permissions (Application Roles) for API by modifying `appRoles` section of App Manifets file:

{% highlight json %}

...
"appRoles": [
		{
			"allowedMemberTypes": [
				"Application"
			],
			"description": "Consumer apps have access to Hi Endpoint",
			"displayName": "CallHelloApiRole",
			"id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
			"isEnabled": true,
			"lang": null,
			"origin": "Application",
			"value": "CallHiApiRole"
		},
		{
			"allowedMemberTypes": [
				"Application"
			],
			"description": "Consumer apps have access to Hello Endpoint",
			"displayName": "CallHiApiRole",
			"id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
			"isEnabled": true,
			"lang": null,
			"origin": "Application",
			"value": "CallHelloApiRole"
		}
	],
  ...

{% endhighlight %}

Notes:

 -  Two app roles were setup (`CallHiApiRole` and `CallHiApiRole`) so they can be granted separately if needed
 -  `Manifest` is available via `Azure AD->App Registrtaions-><Your App>->Manifest`.
 -  `Id` param in role setup must be a GUID and you will need to generate it manually (ex: using [https://www.guidgenerator.com/](https://www.guidgenerator.com/))
 - `Value` param contains the

<br/>

#### Client Registration

1. Register Cleint App in Azure AD by following steps in [https://docs.microsoft.com/en-us/azure/active-directory/develop/quickstart-register-app](https://docs.microsoft.com/en-us/azure/active-directory/develop/quickstart-register-app). These are the same steps as in step #1 of `API Registartion`.

2. Grant API persmissions (App Roles) to Client App Registartion using `Azure AD->App Registrtaions-><Your Client>->API Permisions` screen:
 - Select `Add a permission`
 - Select `My APIs-><Your API>->Application Permissions`
 - Tick `CallHiApiRole` and `CallHelloApiRole`

3. Provdie Admin conecnt to Client for new permissions in `Azure AD->App Registrtaions-><Your Client>->API Permisions` screen by clicking `Grant admin concent for <Your Azure AD Instance>` button.

4. Create Client Secret by using `Azure AD->App Registrtaions-><Your Client>->Certificates and secrets` screen by clicking `New client secret`.

<br/>

#### Veirfy Client and API registartion

Will be checking the setup by performing a `Request Token` call to the Authorization Server (Azure AD):

###### Request

{% highlight bash %}

curl --request POST \
  --url https://login.microsoftonline.com/<azure_ad_tenant_id>/oauth2/v2.0/token \
  --header 'content-type: application/x-www-form-urlencoded' \
  --data grant_type=client_credentials \
  --data scope=api%3A%2F%2F<api_application_id>%2F.default \
  --data client_id=<client_id> \
  --data 'client_secret=<client_secret>'

{% endhighlight %}

Notes:
- `<azure_ad_tenant_id>` is the Tenant ID of your Azure AD instance (`<Azure AD->Overview>`)
- Scope: the scope defines requested scope. For Azure AD the format is `api://<api_application_id>/.default` 
- `<api_application_id>` is the Application ID of the API (`zure AD->App Registrtaions-><Your App>-<Overview>`)
- `<client_id>` is the Application ID of the Client registration in AD (`Azure AD->App Registrtaions-><Your Client>->Overview`)
- `<client_secret>` is the secret defined for Client in step #4 of `Client Registartion` step


###### Response

Will be getting a JWT access token as our response (successfull), ex.:

{% highlight json %}

{
  "typ": "JWT",
  "alg": "RS256",
  "x5t": "CtTuhMJmD5M7DLdzD2v2x3QKSRY",
  "kid": "CtTuhMJmD5M7DLdzD2v2x3QKSRY"
}.{
  "aud": "api://<api_application_id>",
  "iss": "https://sts.windows.net/<azure_ad_tenant_id>/",
  "iat": 1587765136,
  "nbf": 1587765136,
  "exp": 1587769036,
  "aio": "...",
  "appid": "...",
  "appidacr": "1",
  "idp": "https://sts.windows.net/<azure_ad_tenant_id>/",
  "oid": "...",
  "roles": [
    "CallHelloApiRole",
	"CallHiApiRole"
  ],
  "sub": "...",
  "tid": "<azure_ad_tenant_id>d",
  "uti": "...",
  "ver": "1.0"
}.[Signature]

{% endhighlight %}

Notes:
- `aud` claim contains the audience which is your API URI
- `roles` claims contains list of granted permissions for the client for the request scope

<br/>

### References:
* [Azure AD and the Oauth 2.0 Client Credentials Flow](https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-client-creds-grant-flow)
* [Azure AD App Manifest Reference](https://docs.microsoft.com/en-us/azure/active-directory/develop/reference-app-manifest)

 
 {% include custom_footer.html %}