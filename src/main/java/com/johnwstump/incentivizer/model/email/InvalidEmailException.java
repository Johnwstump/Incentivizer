package com.johnwstump.incentivizer.model.email;

public class InvalidEmailException extends Exception {
    InvalidEmailException(String message) {
        super(message);
    }
}
