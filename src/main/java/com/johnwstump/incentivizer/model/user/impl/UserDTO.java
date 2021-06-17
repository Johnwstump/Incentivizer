package com.johnwstump.incentivizer.model.user.impl;

import com.johnwstump.incentivizer.model.password.PasswordMatches;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@PasswordMatches
@NoArgsConstructor
public @Data class UserDTO extends User {
    private String plainPassword;
    private String matchingPlainPassword;
}
