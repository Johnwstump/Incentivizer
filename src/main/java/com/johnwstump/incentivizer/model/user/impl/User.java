package com.johnwstump.incentivizer.model.user.impl;

import com.johnwstump.incentivizer.model.email.EmailValidator;
import com.johnwstump.incentivizer.model.email.InvalidEmailException;
import com.johnwstump.incentivizer.model.user.IUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@NoArgsConstructor
public @Data class User implements IUser {
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String email;

    public User(String name, String email) throws InvalidEmailException {
        setName(name);
        setEmail(email);
    }

    public void setEmail(String email) throws InvalidEmailException {
        email = email.toUpperCase().trim();
        new EmailValidator().validateEmail(email);

        this.email = email;
    }
}
