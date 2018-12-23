package org.davidcampos.cassandra.achilles;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import info.archinnov.achilles.generated.ManagerFactory;
import info.archinnov.achilles.generated.ManagerFactoryBuilder;
import info.archinnov.achilles.generated.manager.UserAchilles_Manager;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.davidcampos.cassandra.commons.Commons;
import org.davidcampos.cassandra.commons.Run;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RunAchilles extends Run {

    private Cluster cluster;
    private Session session;
    private UserAchilles_Manager manager;
    private Map<UUID, UserAchilles> users;


    public RunAchilles() {
        super(LogManager.getLogger(RunAchilles.class));
        this.users = new HashMap<>();
    }

    @Override
    public void open() {
        cluster = Cluster.builder()
                .addContactPoint(Commons.EXAMPLE_CASSANDRA_HOST)
                .build();

        session = cluster.connect();

        ManagerFactory managerFactory = ManagerFactoryBuilder
                .builder(cluster)
                .withDefaultKeyspaceName("example")
                .doForceSchemaCreation(true)
                .build();

        manager = managerFactory.forUserAchilles();
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

            UserAchilles user = new UserAchilles(
                    uuid,
                    "John" + i,
                    "Smith" + i,
                    "London" + i
            );
            users.put(uuid, user);

            Commons.resumeOrStartStopWatch(stopwatch);
            manager
                    .crud()
                    .insert(user)
                    .execute();
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
            UserAchilles user = manager.crud().findById(uuid).get();
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
            UserAchilles user = users.get(uuid);
            user.setFirstName(user.getFirstName() + "___u");
            user.setLastName(user.getLastName() + "___u");
            user.setCity(user.getCity() + "___u");

            Commons.resumeOrStartStopWatch(stopwatch);
            manager.crud().update(user).execute();
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
            UserAchilles user = users.get(uuid);

            Commons.resumeOrStartStopWatch(stopwatch);
            manager.crud().delete(user).execute();
            stopwatch.suspend();
        }

        stopwatch.stop();
        return stopwatch;
    }
}
