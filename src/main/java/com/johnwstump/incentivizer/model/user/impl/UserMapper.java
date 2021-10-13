package com.johnwstump.incentivizer.model.user.impl;

import com.johnwstump.incentivizer.model.email.InvalidEmailException;
import com.johnwstump.incentivizer.model.user.IUser;
import com.johnwstump.incentivizer.model.user.impl.dto.UserDTO;

public class UserMapper {

    public UserMapper() {
    }

    public IUser toUser(UserDTO userDTO) throws InvalidEmailException {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        return user;
    }

    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        return userDTO;
    }
}
