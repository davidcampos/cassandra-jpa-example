#!/usr/bin/env bash
while true; do docker stats --no-stream cassandra-jpa-example_cassandra_1 --format "\t{{.MemUsage}}\t{{.MemPerc}}\t{{.CPUPerc}}" | ts >> stats-cassandra.tsv; done