package org.davidcampos.cassandra.datastax;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.davidcampos.cassandra.commons.Commons;
import org.davidcampos.cassandra.commons.Run;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RunDatastax extends Run {

    private Cluster cluster;
    private Session session;
    private Mapper<UserDatastax> mapper;
    private Map<UUID, UserDatastax> users;


    public RunDatastax() {
        super(LogManager.getLogger(RunDatastax.class));
        this.users = new HashMap<>();
    }

    @Override
    public void open() {
        cluster = Cluster.builder()
                .addContactPoint(Commons.EXAMPLE_CASSANDRA_HOST)
                .build();

        session = cluster.connect();

        session.execute("CREATE KEYSPACE IF NOT EXISTS example" +
                "  WITH REPLICATION = { " +
                "   'class' : 'SimpleStrategy', " +
                "   'replication_factor' : 1 " +
                "  };");

        session.execute("CREATE TABLE IF NOT EXISTS example.user " +
                "( id UUID PRIMARY KEY, first_name text, last_name text, city text);");

        MappingManager manager = new MappingManager(session);
        mapper = manager.mapper(UserDatastax.class);
    }

    @Override
    public void close() {
        session.close();
        cluster.close();
    }

    @Override
    public void clean() {
        open();
        session.execute("TRUNCATE example.user");
        close();
    }

    @Override
    public StopWatch write(int repetition) throws InterruptedException {
        StopWatch stopwatch = new StopWatch();

        for (int i = 0; i < Commons.OPERATIONS; i++) {
            UUID uuid = Commons.uuids.get(repetition * Commons.OPERATIONS + i);

            UserDatastax user = new UserDatastax(
                    uuid,
                    "John" + i,
                    "Smith" + i,
                    "London" + i
            );
            users.put(uuid, user);

            Commons.resumeOrStartStopWatch(stopwatch);
            mapper.save(user);
            stopwatch.suspend();
        }

        stopwatch.stop();
        return stopwatch;
    }

    @Override
    public StopWatch read(int repetition) throws InterruptedException {
        StopWatch stopwatch = new StopWatch();

        for (int i = 0; i < Commons.OPERATIONS; i++) {
            UUID uuid = Commons.uuids.get(repetition * Commons.OPERATIONS + i);

            Commons.resumeOrStartStopWatch(stopwatch);
            UserDatastax user = (UserDatastax) mapper.get(uuid);
            stopwatch.suspend();
        }

        stopwatch.stop();
        return stopwatch;
    }

    @Override
    public StopWatch update(int repetition) throws InterruptedException {
        StopWatch stopwatch = new StopWatch();

        for (int i = 0; i < Commons.OPERATIONS; i++) {
            UUID uuid = Commons.uuids.get(repetition * Commons.OPERATIONS + i);
            UserDatastax user = users.get(uuid);
            user.setFirstName(user.getFirstName() + "___u");
            user.setLastName(user.getLastName() + "___u");
            user.setCity(user.getCity() + "___u");

            Commons.resumeOrStartStopWatch(stopwatch);
            mapper.save(user);
            stopwatch.suspend();
        }

        stopwatch.stop();
        return stopwatch;
    }

    @Override
    public StopWatch delete(int repetition) throws InterruptedException {
        StopWatch stopwatch = new StopWatch();

        for (int i = 0; i < Commons.OPERATIONS; i++) {
            UUID uuid = Commons.uuids.get(repetition * Commons.OPERATIONS + i);
            UserDatastax user = users.get(uuid);

            Commons.resumeOrStartStopWatch(stopwatch);
            mapper.delete(user);
            stopwatch.suspend();
        }

        stopwatch.stop();
        return stopwatch;
    }
}
