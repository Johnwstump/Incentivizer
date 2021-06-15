package com.johnwstump.incentivizer.model.impl;

import com.johnwstump.incentivizer.model.IUser;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public @Data class User implements IUser {
    private String name;
    private String email;

    public User(String name, String email) {
        setName(name);
        setEmail(email);
    }
}
