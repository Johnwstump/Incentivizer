package com.johnwstump.incentivizer.model.password;

import com.johnwstump.incentivizer.model.user.impl.dto.RegistrationRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, RegistrationRequest> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(RegistrationRequest newUser, ConstraintValidatorContext context) {
        return newUser.getPassword().equals(newUser.getMatchingPassword());
    }
}