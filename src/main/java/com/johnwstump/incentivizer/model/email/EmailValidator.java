package com.johnwstump.incentivizer.model.email;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    private static Pattern pattern;

    private static final String EMAIL_PATTERN = "^[A-Z0-9._%+-]++@[A-Z0-9.-]+\\.[A-Z]{2,}+$";

    public void validateEmail(String email) throws InvalidEmailException {
        // We restrict length to prevent potential attacks leveraging
        // regex backtracking
        if (email.length() > 100){
            throw new InvalidEmailException("Email length must be < 100 characters.");
        }

        Matcher matcher = getPattern().matcher(email);

        if (!matcher.matches()){
            throw new InvalidEmailException("Email is not valid.");
        }
    }

    private static Pattern getPattern(){
        if (pattern == null){
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        return pattern;
    }
}
