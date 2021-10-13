package com.johnwstump.incentivizer.services.user.impl;

public class UserAlreadyExistsException extends Exception {
    UserAlreadyExistsException(String message){
        super(message);
    }
}
