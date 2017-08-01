# datetime

This basic micro-service that returns the date and time. Documentation can be found [here](docs/SUMMARY.md).

# Building

```
$ mvn clean install
```

# Building a Docker Container and Deploying

```
$ docker-compose build
```

# Launching application

It is often a good idea that  you test your container locally before pushing
it to the Docker registry. To run the service in your local development environment use

```
$ docker-compose up
```

To test, make a call to the API service

```{HTTP}
GET http://localhost:8080/date-time/v1/health
```

```{HTTP}
GET http://127.0.0.1:8080/date-time/v1/image
```


# Metrics

You can access the [prometheus](https://prometheus.io/) exporter metrics 

```{HTTP}
GET http://127.0.0.1:8080/prometheus
```


# Configuration

The datetime service contains a configuration file in [application.yml](application/src/main/resources/application.yml).
The configuration management system used in Spring Boot will replace the auth-key with the environment variable
AUTH_KEY.

When running in docker-compose, the [docker-compose.yml](docker-compose.yml) file sets this value accordingly to be used in your
environment.
