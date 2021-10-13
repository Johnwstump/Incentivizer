package com.johnwstump.incentivizer.dao;

import com.johnwstump.incentivizer.model.user.impl.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Finds the User with the indicated email, if any.
     *
     * @param email The email which will be used to look for a User
     * @return An optional containing the matching User, if one exists
     */
    Optional<User> findByEmail(String email);
}
