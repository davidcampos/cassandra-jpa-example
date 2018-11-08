package org.davidcampos.cassandra.commons;

import java.util.UUID;

public interface User {
    UUID getId();

    void setId(UUID id);

    String getFirstName();

    void setFirstName(final String firstName);

    String getLastName();

    void setLastName(final String lastName);

    String getCity();

    void setCity(final String city);

    String toString();
}
