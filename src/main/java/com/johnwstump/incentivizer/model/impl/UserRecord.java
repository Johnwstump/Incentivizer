package com.johnwstump.incentivizer.model.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "user")
public @Data class UserRecord extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar created;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updated;

    public UserRecord(String name, String email) {
        super(name, email);
    }
}