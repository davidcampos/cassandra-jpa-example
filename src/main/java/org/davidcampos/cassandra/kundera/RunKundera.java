package org.davidcampos.cassandra.kundera;

import com.impetus.client.cassandra.common.CassandraConstants;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.davidcampos.cassandra.commons.Commons;
import org.davidcampos.cassandra.commons.Run;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RunKundera extends Run {
    private EntityManagerFactory emf;
    private EntityManager em;
    private Map<UUID, UserKundera> users;

    public RunKundera() {
        super(LogManager.getLogger(RunKundera.class));
        this.users = new HashMap<>();
    }

    @Override
    public void open() {
        // Configurations to Cassandra
        Map<String, String> props = new HashMap<>();
        props.put(CassandraConstants.CQL_VERSION, CassandraConstants.CQL_VERSION_3_0);

        // Create connection to Cassandra
        emf = Persistence.createEntityManagerFactory("cassandra_pu", props);
        em = emf.createEntityManager();
    }

    @Override
    public void close() {
        em.close();
        emf.close();
    }

    @Override
    public void clean() {
        open();
        em.createNativeQuery("TRUNCATE user").executeUpdate();
        close();
    }

    @Override
    public StopWatch write(int repetition) throws InterruptedException {
        StopWatch stopwatch = new StopWatch();

        for (int i = 0; i < Commons.ITERATIONS; i++) {
            // Get UUID
            UUID uuid = Commons.uuids.get(repetition * Commons.ITERATIONS + i);

            // Create user
            UserKundera user = new UserKundera(
                    uuid,
                    "John" + i,
                    "Smith" + i,
                    "London" + i
            );
            users.put(uuid, user);

            // Store user
            Commons.resumeOrStartStopWatch(stopwatch);
            em.persist(user);
            stopwatch.suspend();

            // Wait until next
            if (Commons.EXAMPLE_REQUEST_WAIT > 0) {
                Thread.sleep(Commons.EXAMPLE_REQUEST_WAIT);
            }
        }

        stopwatch.stop();
        return stopwatch;
    }

    @Override
    public StopWatch read(int repetition) throws InterruptedException {
        StopWatch stopwatch = new StopWatch();

        for (int i = 0; i < Commons.ITERATIONS; i++) {
            UUID uuid = Commons.uuids.get(repetition * Commons.ITERATIONS + i);

            Commons.resumeOrStartStopWatch(stopwatch);
            UserKundera user = em.find(UserKundera.class, uuid);
            stopwatch.suspend();

            if (Commons.EXAMPLE_REQUEST_WAIT > 0) {
                Thread.sleep(Commons.EXAMPLE_REQUEST_WAIT);
            }
        }

        stopwatch.stop();
        return stopwatch;
    }

    @Override
    public StopWatch update(int repetition) throws InterruptedException {
        StopWatch stopwatch = new StopWatch();

        for (int i = 0; i < Commons.ITERATIONS; i++) {
            UUID uuid = Commons.uuids.get(repetition * Commons.ITERATIONS + i);
            UserKundera user = users.get(uuid);
            user.setFirstName(user.getFirstName() + "___u");
            user.setLastName(user.getLastName() + "___u");
            user.setCity(user.getCity() + "___u");

            Commons.resumeOrStartStopWatch(stopwatch);
            em.merge(user);
            stopwatch.suspend();

            if (Commons.EXAMPLE_REQUEST_WAIT > 0) {
                Thread.sleep(Commons.EXAMPLE_REQUEST_WAIT);
            }
        }

        stopwatch.stop();
        return stopwatch;
    }

    @Override
    public StopWatch delete(int repetition) throws InterruptedException {
        StopWatch stopwatch = new StopWatch();

        for (int i = 0; i < Commons.ITERATIONS; i++) {
            UUID uuid = Commons.uuids.get(repetition * Commons.ITERATIONS + i);
            UserKundera user = users.get(uuid);

            Commons.resumeOrStartStopWatch(stopwatch);
            em.remove(user);
            stopwatch.suspend();

            if (Commons.EXAMPLE_REQUEST_WAIT > 0) {
                Thread.sleep(Commons.EXAMPLE_REQUEST_WAIT);
            }
        }

        stopwatch.stop();
        return stopwatch;
    }
}
