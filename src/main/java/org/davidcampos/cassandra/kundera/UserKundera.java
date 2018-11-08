package org.davidcampos.cassandra.kundera;

import com.google.common.base.MoreObjects;
import org.davidcampos.cassandra.commons.User;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user", schema = "example@cassandra_pu")
public class UserKundera implements User {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "city")
    private String city;

    public UserKundera(UUID id, String firstName, String lastName, String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
    }

    public UserKundera() {
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
