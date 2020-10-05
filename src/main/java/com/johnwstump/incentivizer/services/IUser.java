package com.johnwstump.incentivizer.services;

import com.johnwstump.incentivizer.model.User;

import java.util.List;
import java.util.Optional;

public interface IUser {
    List<User> getAllUsers();
    Optional<User> findById(Long id);
    User save (User user);
    void deleteById(Long id);
}
