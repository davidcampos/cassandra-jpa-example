Cassandra with JPA: Datastax vs. Kundera vs. Achilles
===
Example project on how to use [Apache Cassandra](http://cassandra.apache.org/) with reference JPA libraries, namely:
- [Datastax](https://github.com/datastax/java-driver)
- [Kundera](https://github.com/Impetus/Kundera)
- [Achilles](https://github.com/doanduyhai/Achilles)

Requirements
---
- Docker
- Docker Compose
- Java 8
- Maven

Build
---
1. Build Java project
    ```
    mvn clean package
    ```
1. Build Docker image 
    ```
    docker build -t cassandra-jpa-example .
    ```

Run
---
1. Start docker containers
    ```
    docker-compose up -d
    ```

Check
---

1. Check program logs:
    ```
    docker logs cassandra-jpa-example_java_1 -f
    ```
    
    Output should be similar to:
    ```
    21:19:10.688 [main] INFO  org.davidcampos.cassandra.datastax_native.RunDatastaxNative - 	WRITE	2	1956	1223	733	978.0
    21:19:14.508 [main] INFO  org.davidcampos.cassandra.datastax_native.RunDatastaxNative - 	READ	2	1269	684	585	634.5
    21:19:18.038 [main] INFO  org.davidcampos.cassandra.datastax_native.RunDatastaxNative - 	DELETE	2	1054	538	516	527.0
    ```

Stop
---
1. Stop docker containers
    ```
    docker-compose down
    ```
