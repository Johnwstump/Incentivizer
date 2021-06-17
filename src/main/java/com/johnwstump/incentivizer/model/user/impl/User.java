package com.johnwstump.incentivizer.model.user.impl;

import com.johnwstump.incentivizer.model.email.ValidEmail;
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
    @ValidEmail
    private String email;

    public User(String name, String email) {
        setName(name);
        setEmail(email);
    }
}
