# Technical Challenge

This project is the result of the technical challenge proposed by **Naiz.fit**

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

### Requirements

- Java 17
- Mongo

Fill `src/main/resources/application.properties` file with the corresponding address for mongo, and the appropriated credentials (default
in dev: no credentials)

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Swagger page
> available [here](http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/swagger-ui)

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a Docker image

You can create a native executable using this two commands:

```shell script
./mvnw clean package
```

```shell sctip
docker build -f src/main/Docker/Dockerfile.jvm -t pgomez/technical-challenge .
```

To run it, simply:

```shell script
docker run -i --rm -p 8080:8080 pgomez/technical-challenge
```


## Running Mongo

In order to run mongo in docker simply:

```
docker run --name mongo -d mongo:latest
```
