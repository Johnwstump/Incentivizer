package com.johnwstump.incentivizer.model.user.impl.dto;

import com.johnwstump.incentivizer.model.password.PasswordMatches;
import lombok.Data;

@PasswordMatches
public @Data
class RegistrationRequest {

    private String name;
    private String email;
    private String password;
    private String matchingPassword;

}
