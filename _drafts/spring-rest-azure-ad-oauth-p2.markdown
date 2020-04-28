---
layout: post
title:  "Secure REST API with OAuth 2.0 Client Credentials Flow using Azure AD."
date:   2020-04-27 09:00:01 -0400
categories: java spring rest azuread oauth2
---
### Introduction


## Part 2. Spring REST API configuration

### Spring Boot REST API Example

Define a dummy Spring Boot REST API with 2 endpoints 'hi' and 'hello'.

{% highlight java %}

...

@RestController
@RequestMapping("/api/v1")
public class GreetingController {

    @GetMapping("/hi")
    public String hi() {
        return "Hi";
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name", required = true)
    final String name) {
        return String.format("Hello, %s", name);
    }
}
...

{% endhighlight %}


<br/>

### OAuth 2.0 Resource Server

#### Configuration steps

1.  Create custom `ResourceServerConfig` class which extends `ResourceServerConfigurerAdapter` and is annotated with `@Configuration`.

2. Enable resource server by adding `@EnableResourceServer` annottation to. 

3. Enable Web Security by adding `@EnableWebSecurity` annotation to `ResourceServerConfig` class

4. Enable method level authorization by adding `@EnableGlobalMethodSecurity(prePostEnabled = true)`

{% highlight java %}

...
@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
  ...

{% endhighlight %}

5. Explicitely set Resource ID for our resource server - it must be the same as our API URI which was defined in Part 1 and used as `scope` in access token request (without '.default').

{% highlight java %}

...
@Override
public void configure(final ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId("api://<api_application_id>").stateless(true);
}
  ...

{% endhighlight %}

6. Customize roles/authorties

Notes:

7. Protect the endpoints of REST API with different roles/authorities by applying method level authorization configuration using `@PreAuthorize` annotation of `Spring Security`

{% highlight java %}

...

    @PreAuthorize("hasAuthority('SCOPE_CallHiApiRole')")
    @GetMapping("/hi")
    public String hi() {
        return "Hi";
    }

    @PreAuthorize("hasAuthority('SCOPE_CallHelloApiRole')")
    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name", required = true)
    final String name) {
        return String.format("Hello, %s", name);
    }

...

{% endhighlight %}

Note:
 - Role/authority names match the `Application Roles` defined in Azure AD and have `SCOPE_` prefix.
 - Use `hasAuthority` Security EL instead of `hasRole`





#### Verify Resource Server setup

Will be checking the setup by performing a request to our API suing `Access Token` obtained from our AUthorization Server (see Part 1).

<b>Request</b>

{% highlight http %}

curl --request GET \
  --url http://localhost:8082/api/v1/hi \
  --header 'authorization: Bearer <access_token>'

{% endhighlight %}

Notes:
- `<azure_ad_tenant_id>` is the Tenant ID of your Azure AD instance (`<Azure AD->Overview>`)
- Scope: the scope defines requested scope. For Azure AD the format is `api://<api_application_id>/.default` 
- `<api_application_id>` is the Application ID of the API (`Azure AD->App Registrations-><Your App>-<Overview>`)
- `<client_id>` is the Application ID of the Client registration in AD (`Azure AD->App Registrations-><Your Client>->Overview`)
- `<client_secret>` is the secret defined for Client in step #4 of `Client Registration` step

<b>Response</b>

Will be getting a JWT access token as our response (successful), ex.:

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

<br />

### References:
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Spring Security: Configure Authorization)](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2resourceserver-jwt-authorization)

 
 {% include custom_footer.html %}