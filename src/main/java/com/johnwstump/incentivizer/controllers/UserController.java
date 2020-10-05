package com.johnwstump.incentivizer.controllers;

import com.johnwstump.incentivizer.dto.UserRepository;
import com.johnwstump.incentivizer.model.User;
import com.johnwstump.incentivizer.services.IUser;
import com.johnwstump.incentivizer.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    IUser userService;

    @GetMapping("/")
    public List<User> getUsers(@PathVariable long nameId) {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        Optional<User> retrievedUser = userService.findById(userId);

        if (!retrievedUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return retrievedUser.get();
    }

    @PostMapping("/")
    public User addUser(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteById(userId);
    }
}
