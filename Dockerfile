#
# (C) Copyright 2016 HP Development Company, L.P.
# Confidential computer software. Valid license from HP required for possession, use or copying.
# Consistent with FAR 12.211 and 12.212, Commercial Computer Software,
# Computer Software Documentation, and Technical Data for Commercial Items are licensed
# to the U.S. Government under vendor's standard commercial license.
#

FROM maven:3.5.0-jdk-8-alpine
WORKDIR /app
COPY . /app
RUN mvn clean install


FROM openjdk:8-jre-alpine
MAINTAINER Galo Gimenez <galo@hp.com>

# Used for debugging
#RUN apk update && apk add --no-cache curl ca-certificates wget && update-ca-certificates

# Add datetime configuration
RUN mkdir -p /app

# Tomcat
VOLUME /tmp

# Add the entry point
ADD ./build/docker/run.sh /app/run.sh
RUN chmod 755 /app/run.sh

# Add the entry point
EXPOSE 80 80

CMD ["/app/run.sh"]

# Service ----------------------------------------------------

# Add executable JAR file and change permissions
COPY --from=0 /app/application/target/ccp-crs-datetime.jar /app/ccp-crs-datetime.jar
RUN sh -c 'touch /app/ccp-crs-datetime.jar'
