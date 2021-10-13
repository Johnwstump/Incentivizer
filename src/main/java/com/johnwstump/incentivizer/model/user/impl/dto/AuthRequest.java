package com.johnwstump.incentivizer.model.user.impl.dto;

import lombok.Data;

public @Data
class AuthRequest {

    private String username;
    private String password;
}
