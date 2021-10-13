package com.johnwstump.incentivizer.model.user.impl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public @Data
class UserDTO {
    private long id;
    private String name;
    private String email;
}
