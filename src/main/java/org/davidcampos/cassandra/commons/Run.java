package org.davidcampos.cassandra.commons;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.TimeUnit;

abstract public class Run {

    private Logger logger;

    public Run(final Logger logger) {
        this.logger = logger;
    }

    public void run() throws InterruptedException {
        for (int i = 0; i < Commons.CYCLES; i++) {
            Commons.uuids = Commons.generateUUIDs();
            clean();

            LongSummaryStatistics statsWrite = runWrites();
            Commons.logStatistics(logger, "WRITE", statsWrite);

            LongSummaryStatistics statsRead = runReads();
            Commons.logStatistics(logger, "READ", statsRead);

            LongSummaryStatistics statsUpdate = runUpdates();
            Commons.logStatistics(logger, "UPDATE", statsUpdate);

            LongSummaryStatistics statsDelete = runDeletes();
            Commons.logStatistics(logger, "DELETE", statsDelete);

            logger.info("");
        }
    }

    abstract public void open();

    abstract public void close();

    abstract public void clean();

    public LongSummaryStatistics runWrites() throws InterruptedException {
        // Open connection
        open();

        // Write
        List<StopWatch> stopwatches = new ArrayList<>();
        for (int i = 0; i < Commons.REPETITIONS; i++) {
            stopwatches.add(write(i));
        }

        // Get statistics
        LongSummaryStatistics stats = stopwatches.stream()
                .mapToLong((x) -> x.getTime(TimeUnit.MILLISECONDS))
                .summaryStatistics();

        // Close connection
        close();

        return stats;
    }

    abstract public StopWatch write(final int repetition) throws InterruptedException;

    public LongSummaryStatistics runReads() throws InterruptedException {
        // Open connection
        open();

        // Read
        List<StopWatch> stopwatches = new ArrayList<>();
        for (int i = 0; i < Commons.REPETITIONS; i++) {
            stopwatches.add(read(i));
        }

        // Get statistics
        LongSummaryStatistics stats = stopwatches.stream()
                .mapToLong((x) -> x.getTime(TimeUnit.MILLISECONDS))
                .summaryStatistics();

        // Close connection
        close();

        return stats;
    }

    abstract public StopWatch read(final int repetition) throws InterruptedException;

    public LongSummaryStatistics runUpdates() throws InterruptedException {
        // Open connection
        open();

        // Write
        List<StopWatch> stopwatches = new ArrayList<>();
        for (int i = 0; i < Commons.REPETITIONS; i++) {
            stopwatches.add(update(i));
        }

        // Get statistics
        LongSummaryStatistics stats = stopwatches.stream()
                .mapToLong((x) -> x.getTime(TimeUnit.MILLISECONDS))
                .summaryStatistics();

        // Close connection
        close();

        return stats;
    }

    abstract public StopWatch update(final int repetition) throws InterruptedException;

    public LongSummaryStatistics runDeletes() throws InterruptedException {
        // Open connection
        open();

        // Write
        List<StopWatch> stopwatches = new ArrayList<>();
        for (int i = 0; i < Commons.REPETITIONS; i++) {
            stopwatches.add(delete(i));
        }

        // Get statistics
        LongSummaryStatistics stats = stopwatches.stream()
                .mapToLong((x) -> x.getTime(TimeUnit.MILLISECONDS))
                .summaryStatistics();

        // Close connection
        close();

        return stats;
    }

    abstract public StopWatch delete(final int repetition) throws InterruptedException;
}
