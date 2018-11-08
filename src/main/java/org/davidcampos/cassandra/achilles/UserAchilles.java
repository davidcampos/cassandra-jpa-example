package org.davidcampos.cassandra.achilles;


import com.google.common.base.MoreObjects;
import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.PartitionKey;
import info.archinnov.achilles.annotations.Table;
import org.davidcampos.cassandra.commons.User;

import java.util.UUID;


@Table(table = "user")
public class UserAchilles implements User {
    @Column(value = "id")
    @PartitionKey
    private UUID id;

    @Column(value = "first_name")
    private String firstName;

    @Column(value = "last_name")
    private String lastName;

    @Column(value = "city")
    private String city;

    public UserAchilles(UUID id, String firstName, String lastName, String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
    }

    public UserAchilles() {
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(final String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("city", city)
                .toString();
    }
}
