package com.johnwstump.incentivizer.services;

import com.johnwstump.incentivizer.model.IUser;
import com.johnwstump.incentivizer.model.impl.UserRecord;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserRecord> getAllUsers();
    Optional<UserRecord> findById(long id);
    UserRecord save (IUser user);
    UserRecord save (long id, IUser user);
    void deleteById(long id);
}
