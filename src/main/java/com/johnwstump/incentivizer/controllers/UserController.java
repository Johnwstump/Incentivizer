package com.johnwstump.incentivizer.controllers;

import com.johnwstump.incentivizer.model.User;
import com.johnwstump.incentivizer.services.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    IUser userService;

    @GetMapping("/")
    public List<User> getUsers() {
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
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User addedUser = userService.save(user);

        if (addedUser == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{userId}").buildAndExpand(addedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteById(userId);
    }
}
