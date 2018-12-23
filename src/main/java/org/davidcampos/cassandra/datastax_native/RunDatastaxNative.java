package org.davidcampos.cassandra.datastax_native;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.*;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.davidcampos.cassandra.commons.Commons;
import org.davidcampos.cassandra.commons.Run;

import java.util.UUID;

public class RunDatastaxNative extends Run {

    private Cluster cluster;
    private Session session;

    public RunDatastaxNative() {
        super(LogManager.getLogger(RunDatastaxNative.class));
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

            Insert insert = QueryBuilder
                    .insertInto("example", "user")
                    .value("id", uuid)
                    .value("first_name", "John" + i)
                    .value("last_name", "Smith" + i)
                    .value("city", "London" + i);


            Commons.resumeOrStartStopWatch(stopwatch);
            session.execute(insert);
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

            Select.Where select = QueryBuilder
                    .select("id", "first_name", "last_name", "city")
                    .from("example", "user")
                    .where(QueryBuilder.eq("id", uuid));

            Commons.resumeOrStartStopWatch(stopwatch);
            ResultSet rs = session.execute(select);
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

            Update.Where update = QueryBuilder
                    .update("example", "user")
                    .with(QueryBuilder.set("first_name", "___u"))
                    .and(QueryBuilder.set("last_name", "___u"))
                    .and(QueryBuilder.set("city", "___u"))
                    .where(QueryBuilder.eq("id", uuid));

            Commons.resumeOrStartStopWatch(stopwatch);
            ResultSet rs = session.execute(update);
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

            Delete.Where delete = QueryBuilder
                    .delete()
                    .from("example", "user")
                    .where(QueryBuilder.eq("id", uuid));

            Commons.resumeOrStartStopWatch(stopwatch);

            session.execute(delete);
            stopwatch.suspend();
        }

        stopwatch.stop();
        return stopwatch;
    }
}
