#!/usr/bin/env bash
while true; do docker stats --no-stream cassandra-jpa-example_java_1 --format "\t{{.MemUsage}}\t{{.MemPerc}}\t{{.CPUPerc}}" | ts >> stats-java.tsv; done