package com.johnwstump.incentivizer.model.user.impl;

import com.johnwstump.incentivizer.model.email.EmailValidator;
import com.johnwstump.incentivizer.model.email.InvalidEmailException;
import com.johnwstump.incentivizer.model.user.IUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Calendar;

@Entity
@NoArgsConstructor
@Table(name = "user")
public @Data class User implements IUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @Column(nullable = false, length = 200)
    private String name;

    @NotEmpty
    @Column(nullable = false, length = 200)
    private String email;

    @NotEmpty
    @Column(nullable = false, length = 100)
    private String encryptedPassword;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false)
    private Calendar created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false)
    private Calendar updated;

    /**
     * Builds an instance of 'User' from the provided name and email.
     *
     * @param name  The name for the new user.
     * @param email The email for the new user.
     * @throws InvalidEmailException If the email provided is not valid.
     */
    public User(String name, String email) throws InvalidEmailException {
        setName(name);
        setEmail(email);
    }

    /**
     * Validates and sets the provided email for this user.
     * @param email The email which will be set, if valid.
     * @throws InvalidEmailException If the email provided is not valid.
     */
    public void setEmail(String email) throws InvalidEmailException {
        email = email.toUpperCase().trim();
        new EmailValidator().validateEmail(email);

        this.email = email;
    }
}