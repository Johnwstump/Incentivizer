package com.johnwstump.incentivizer.services.user;

import com.johnwstump.incentivizer.model.email.InvalidEmailException;
import com.johnwstump.incentivizer.model.password.InvalidPasswordException;
import com.johnwstump.incentivizer.model.user.IUser;
import com.johnwstump.incentivizer.model.user.impl.User;
import com.johnwstump.incentivizer.model.user.impl.dto.RegistrationRequest;
import com.johnwstump.incentivizer.services.user.impl.UserAlreadyExistsException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers();

    Optional<User> findById(long id);

    User registerNewUser(@Valid RegistrationRequest request) throws UserAlreadyExistsException, InvalidEmailException,
            InvalidPasswordException;

    User save(long id, IUser user) throws InvalidEmailException;
    void deleteById(long id);

    Optional<User> findByEmail(String email);
}
