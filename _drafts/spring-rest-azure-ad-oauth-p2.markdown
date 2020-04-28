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

6. Update Security Configuration and require authentication for all API endpoints:

{% highlight java %}

...
    @Override
    public void configure(final HttpSecurity http) throws Exception {

        http
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/api/v1/**").authenticated()
                        .antMatchers("/").permitAll());
    }

...

{% endhighlight %}

7. By default Spring Security expects `authorities` claim in `Access Token`. However Azure AD populates `roles` claim with the Applciation Roles. Will need to customize the default behaviour by defining a custom implementation of `JwtAuthenticationConverter`:


{% highlight java %}

...
    @Override
    public void configure(final HttpSecurity http) throws Exception {

        http
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/api/v1/**").authenticated()
                        .antMatchers("/").permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
  ...

{% endhighlight %}

8. Spring Security needs to know the location of the public key which was used to sign the token so token signature valication can be performed. Azure AD has several active private keys and uses one of them to sign the token. The public keys are available at [https://login.microsoftonline.com/common/discovery/v2.0/keys](https://login.microsoftonline.com/common/discovery/v2.0/keys). The actual key id is defined in `kid` claim in the `access token`. Will need to provide JWK URI to SPring  Security so it knows where to grab the keys.

{% highlight java %}

...
   @Override
    public void configure(final HttpSecurity http) throws Exception {

        http
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/api/v1/**").authenticated()
                        .antMatchers("/").permitAll()) // ;
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                .jwkSetUri("https://login.microsoftonline.com/common/discovery/v2.0/keys")));
      }

...

{% endhighlight %}

9. Will protect the endpoints of REST API with different roles/authorities by applying method level authorization configuration using `@PreAuthorize` annotation of `Spring Security` in the controller:

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
- `<access_token>` is the Tenant ID of your Azure AD instance (`<Azure AD->Overview>`)

<b>Response. Example - Successful</b>

{% highlight http %}

HTTP/1.1 200 
...
Hi

{% endhighlight %}


<b>Response. Example - Access Denied</b>
In case proper application role is not granted as permission to the client we will get a 403 error:

{% highlight http %}

HTTP/1.1 403 
...

{"error":"access_denied","error_description":"Access is denied"}

{% endhighlight %}

<br />

### References:
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Spring Security: Configure Authorization)](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2resourceserver-jwt-authorization)

 
 {% include custom_footer.html %}