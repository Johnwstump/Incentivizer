package com.johnwstump.incentivizer.model.email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static Pattern pattern;

    private static final String EMAIL_PATTERN = "^[A-Z0-9._%+-]++@[A-Z0-9.-]+\\.[A-Z]{2,}+$";

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        // No Initialization needed
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context){
        return (validateEmail(email));
    }

    private boolean validateEmail(String email) {
        // We restrict length to prevent potential attacks leveraging
        // regex backtracking
        if (email.length() > 100){
            return false;
        }

        Matcher matcher = getPattern().matcher(email);
        return matcher.matches();
    }

    private static Pattern getPattern(){
        if (pattern == null){
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        return pattern;
    }
}
