# docker-compose-service-db

Demo project with a services and db setup in 2 Docker containers and run with Docker Compose. 

## Details

### database
MySQL DB

### data-rest-api
REST Service built with [Spring Data REST](https://spring.io/projects/spring-data-rest) which reads and writes to DB

### Build and run

#### Build data-rest-api

```
./mvnw clean install
```

#### Build and run with Docker Compose
```
docker-compose up
```



