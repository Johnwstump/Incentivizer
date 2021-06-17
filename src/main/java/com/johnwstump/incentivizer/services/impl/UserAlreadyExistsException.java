package com.johnwstump.incentivizer.services.impl;

public class UserAlreadyExistsException extends Exception {
    UserAlreadyExistsException(String message){
        super(message);
    }
}
