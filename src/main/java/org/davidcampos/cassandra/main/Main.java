package org.davidcampos.cassandra.main;

import org.davidcampos.cassandra.achilles.RunAchilles;
import org.davidcampos.cassandra.datastax.RunDatastax;
import org.davidcampos.cassandra.kundera.RunKundera;
import org.davidcampos.cassandra.datastax_native.RunDatastaxNative;

public class Main {
    public static void main(final String... args) throws InterruptedException {
        RunDatastaxNative runDatastaxNative = new RunDatastaxNative();
        runDatastaxNative.run();

        runDatastaxNative = new RunDatastaxNative();
        runDatastaxNative.run();

        RunDatastax runDatastax = new RunDatastax();
        runDatastax.run();

        RunKundera runKundera = new RunKundera();
        runKundera.run();

        RunAchilles runAchilles = new RunAchilles();
        runAchilles.run();
    }
}
