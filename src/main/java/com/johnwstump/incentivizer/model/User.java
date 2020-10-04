package com.johnwstump.incentivizer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@NoArgsConstructor
@Table(name = "user")
public @Data class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar created;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updated;

    public User(String name, String email) {
        setName(name);
        setEmail(email);
    }
}