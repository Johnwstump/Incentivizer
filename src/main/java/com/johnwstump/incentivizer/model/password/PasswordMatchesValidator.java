package com.johnwstump.incentivizer.model.password;

import com.johnwstump.incentivizer.model.user.impl.UserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, UserDTO> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(UserDTO newUser, ConstraintValidatorContext context){
        return newUser.getPlainPassword().equals(newUser.getMatchingPlainPassword());
    }
}