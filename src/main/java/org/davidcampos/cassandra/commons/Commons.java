package org.davidcampos.cassandra.commons;

import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.UUID;

import org.apache.logging.log4j.Logger;

public class Commons {


    public final static int ITERATIONS = System.getenv("EXAMPLE_ITERATIONS") != null ?
            Integer.parseInt(System.getenv("EXAMPLE_ITERATIONS")) : 1000;

    public final static int REPETITIONS = System.getenv("EXAMPLE_REPETITIONS") != null ?
            Integer.parseInt(System.getenv("EXAMPLE_REPETITIONS")) : 5;

    public static List<UUID> uuids = generateUUIDs();

    public final static String EXAMPLE_CASSANDRA_HOST = System.getenv("EXAMPLE_CASSANDRA_HOST") != null ?
            System.getenv("EXAMPLE_CASSANDRA_HOST") : "cassandra";

    public final static String EXAMPLE_CASSANDRA_PORT = System.getenv("EXAMPLE_CASSANDRA_PORT") != null ?
            System.getenv("EXAMPLE_CASSANDRA_PORT") : "9160";

    public final static long EXAMPLE_REQUEST_WAIT = System.getenv("EXAMPLE_REQUEST_WAIT") != null ?
            Long.parseLong(System.getenv("EXAMPLE_REQUEST_WAIT")) : 2;

    public static List<UUID> generateUUIDs() {
        List<UUID> uuids = new ArrayList<>();
        for (int i = 0; i < Commons.ITERATIONS * Commons.REPETITIONS; i++) {
            uuids.add(UUID.randomUUID());
        }
        return uuids;
    }

    public static void resumeOrStartStopWatch(final StopWatch stopwatch) {
        if (stopwatch.isSuspended()) {
            stopwatch.resume();
        } else {
            stopwatch.start();
        }
    }

    public static void logStatistics(final Logger logger, final String text, final LongSummaryStatistics stats) {
        logger.info("\t{}\t{}\t{}\t{}\t{}\t{}", text, stats.getCount(), stats.getSum(), stats.getMax(), stats.getMin(),
                stats.getAverage());
    }

}
