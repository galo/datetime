# datetime

[![Build Status](https://flintknapper.george.hpccp.net/buildStatus/icon?job=gdrs/datetime/master)](https://flintknapper.george.hpccp.net/job/gdrs/job/datetime/job/master/)

This basic micro-service that returns the date and time. Documentation can be found [here](docs/SUMMARY.md).

# Building

The build process uses the [George](https://github.azc.ext.hp.com/cwp/george) environment for  [Maven](https://github.azc.ext.hp.com/cwp/george/blob/master/docs/poc-cwp/maven-build.md). Clone the environment and launch [Vagrant](https://www.vagrantup.com/) as described in the [GCD Dev Env](https://github.azc.ext.hp.com/cwp/gcd-devenv) guide. Once inside the environment you can use Maven, Docker and Docker-Compose commands.

```
$ mvn clean install
```

# Building a Docker Container and Deploying

You can build the container directly with docker

```
$ docker build -t hpccp/datetime --build-arg http_proxy=$http_proxy --build-arg https_proxy=$https_proxy .
```

Or launch the build process from docker-compose, this will use your
environment proxy settings directly.

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

# Running functional tests through a maven container

There is a 'functional-tests' profile to run all tests on datetime
service that can be triggered by a the following command.

```
$ docker run -ti --rm -v ~/shared/.m2:/root/.m2 -v $PWD:/app -w /app --link datetime --network datetime_default maven:3.3.9-jdk-8-alpine mvn clean test -P functional-tests
```

Where 'datetime' is the container name of the service and the
network 'datetime_default' is the docker network created by docker-compose.

# Configuration

The datetime service contains a configuration file in [application.yml](application/src/main/resources/application.yml).
The configuration management system used in Spring Boot will replace the auth-key with the environment variable
AUTH_KEY.

When running in docker-compose, the [docker-compose.yml](docker-compose.yml) file sets this value accordingly to be used in your
environment.

# Kubernetes Deployment

You can deploy this service using [Minikube](https://github.com/kubernetes/minikube), Google Cloud Platform, or the GCD model. The next sections describe how to deploy the service on these environments.

## Istio & Google Container Engine

You can use google toolset to deploy, the following guide goes over teh steps to deploy teh service with [Istio](https://istio.io/) and Kubernetes take a look at the [Istio Deployment](build/istio/README.md)


## Canary deployments

This [guide describes](build/canary-istio/README.md) how to do canary deployments of multiple versions of the service, sending some amount of traffic to a newer version before pulling out the old one.

# GCD & Kubernetes

To deploy this service in Kubernetes take a look at the [Kubernetes Deployment Guidelines](build/k8s/README.md)

## Minikube

TBD
