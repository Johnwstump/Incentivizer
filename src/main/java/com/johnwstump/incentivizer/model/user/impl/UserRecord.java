package com.johnwstump.incentivizer.model.user.impl;

import com.johnwstump.incentivizer.model.email.InvalidEmailException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "user")
public @Data class UserRecord extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotEmpty
    private String encryptedPassword;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar created;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updated;

    public UserRecord(String name, String email) throws InvalidEmailException {
        super(name, email);
    }
}