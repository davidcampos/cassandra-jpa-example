FROM openjdk:8u151-jdk-alpine3.7
MAINTAINER David Campos (david.marques.campos@gmail.com)

# Install Bash
RUN apk add --no-cache bash

# Copy resources
WORKDIR /
COPY wait-for-it.sh wait-for-it.sh
COPY target/cassandra-jpa-example-1.0-SNAPSHOT-jar-with-dependencies.jar cassandra-jpa-example.jar

# Wait for Cassandra and Kafka to be available and run application
CMD ./wait-for-it.sh -s -t 180 $EXAMPLE_CASSANDRA_HOST:$EXAMPLE_CASSANDRA_PORT -- java -Xmx512m -jar cassandra-jpa-example.jar