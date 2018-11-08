package org.davidcampos.cassandra.achilles;

import info.archinnov.achilles.annotations.CompileTimeConfig;
import info.archinnov.achilles.type.CassandraVersion;

@CompileTimeConfig(cassandraVersion = CassandraVersion.CASSANDRA_3_0_X)
public interface AchillesConfig {
}
