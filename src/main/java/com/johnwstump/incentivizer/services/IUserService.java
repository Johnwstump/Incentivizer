package com.johnwstump.incentivizer.services;

import com.johnwstump.incentivizer.model.email.InvalidEmailException;
import com.johnwstump.incentivizer.model.user.IUser;
import com.johnwstump.incentivizer.model.user.impl.UserDTO;
import com.johnwstump.incentivizer.model.user.impl.UserRecord;
import com.johnwstump.incentivizer.services.impl.UserAlreadyExistsException;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserRecord> getAllUsers();
    Optional<UserRecord> findById(long id);
    UserRecord registerNewUser (UserDTO user) throws UserAlreadyExistsException, InvalidEmailException;
    UserRecord save (long id, IUser user) throws InvalidEmailException;
    void deleteById(long id);
}
