# spring-boot-azure-ad-auth
Secure a Spring Boot Based API with Azure AD

## projects

- `mvc` - dummy MVC app (Spring MVC with Thymeleaf)
- `api` and `client` - dummy REST API and cleint app/API consumer (Spring REST)

## inital doc and tutorials used

### MVC App - Authorization Code Flow
- [Secure a Java web app using the Spring Boot Starter for Azure Active Directory](https://docs.microsoft.com/en-us/azure/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-active-directory)


### REST API - Client Credentails Flow (M2M)
- [Azure AD Authentication for a Java REST API Resource Server](http://www.redbaronofazure.com/?p=7607)

Note: App Registration (registered App within Azure AD) exposes OAuth2.0 v1 and v2 token and authorization endpoints (`Home-><Your Azure AD>->App Registartion-><Your REST API>->Endpoints`)


