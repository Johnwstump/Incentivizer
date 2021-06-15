package com.johnwstump.incentivizer.rest;

import com.johnwstump.incentivizer.model.impl.User;
import com.johnwstump.incentivizer.model.impl.UserRecord;
import com.johnwstump.incentivizer.services.IUserService;
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
        UserRecord addedUser = userService.save(user);

        if (addedUser == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{userId}").buildAndExpand(addedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
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

        userService.save(userId, user);
    }
}
