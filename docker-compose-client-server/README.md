# docker-compose-client-server

Demo project with 2 services (client->api) setup in 2 Docker containers and run with Docker Compose. 

## Details

### rest-api
REST API build with [Qurkus](https://quarkus.io/)

### rest-client
Cleint Service built with [Spring Boot](https://spring.io/projects/spring-boot) which consumes rest-api API

### Build and run

#### Build projects
```
./rest-api/mvnw clean install
```
```
./rest-client/mvnw clean install
```

#### Build and run with Docker Compose
```
docker-compose up
```



