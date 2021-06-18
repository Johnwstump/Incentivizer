package com.johnwstump.incentivizer.rest;

import com.johnwstump.incentivizer.model.email.InvalidEmailException;
import com.johnwstump.incentivizer.model.user.impl.User;
import com.johnwstump.incentivizer.model.user.impl.UserRecord;
import com.johnwstump.incentivizer.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private IUserService userService;

    @Autowired
    public UserController (IUserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
    public List<UserRecord> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserRecord getUser(@PathVariable Long userId) {
        Optional<UserRecord> retrievedUser = userService.findById(userId);

        if (retrievedUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return retrievedUser.get();
    }

    @PostMapping("/")
    public ResponseEntity<UserRecord> addUser(@RequestBody User user) {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId){
        userService.deleteById(userId);
    }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable long userId, @RequestBody User user){
        if (userService.findById(userId).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        try {
            userService.save(userId, user);
        } catch (InvalidEmailException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
