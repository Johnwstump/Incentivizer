package com.johnwstump.incentivizer.services.user.impl;

import com.johnwstump.incentivizer.dao.UserRepository;
import com.johnwstump.incentivizer.model.email.InvalidEmailException;
import com.johnwstump.incentivizer.model.password.InvalidPasswordException;
import com.johnwstump.incentivizer.model.user.IUser;
import com.johnwstump.incentivizer.model.user.impl.User;
import com.johnwstump.incentivizer.model.user.impl.dto.RegistrationRequest;
import com.johnwstump.incentivizer.services.user.IUserService;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordValidator passwordValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public User registerNewUser(@Valid RegistrationRequest request) throws UserAlreadyExistsException, InvalidEmailException, InvalidPasswordException {
        if (userWithEmailExists(request.getEmail())) {
            throw new UserAlreadyExistsException("A user already exists with that email address.");
        }

        validatePassword(request.getPassword(), this.passwordValidator);

        User record = new User();
        record.setName(request.getName());
        record.setEmail(request.getEmail());
        record.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(record);
    }

    /**
     * Using the a PasswordValidator, determines whether the provided password is valid.
     * If it not, an {@link InvalidPasswordException InvalidPasswordException} is thrown.
     *
     * @param password  The password which will be tested for validity.
     * @param validator The Validator which will be used to validate the password.
     * @throws InvalidPasswordException If the password provided is not valid.
     */
    private void validatePassword(String password, PasswordValidator validator) throws InvalidPasswordException {
        RuleResult result = validator.validate(new PasswordData(password));

        if (!result.isValid()) {
            throw new InvalidPasswordException(validator.getMessages(result));
        }
    }

    /**
     * Indicates whether a user exists with the provided email.
     *
     * @param email The email which will be used to look for an associated user.
     * @return Whether there is a user associated with the provided email.
     */
    private boolean userWithEmailExists(String email) {
        return findByEmail(email).isPresent();
    }

    /**
     * Finds the user associated with the provided email, if one exists.
     *
     * @param email The email which will be used to look for an associated user.
     * @return An optional containing the user found, if any.
     */
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email.toUpperCase());
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    /**
     * Finds the user associated with the provided id, if one exists.
     * @param id The id of the user which will be retrieved.
     * @return An optional containing the user found, if any.
     */
    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(long id, IUser user) throws InvalidEmailException {
        Optional<User> possibleRecordToUpdate = findById(id);

        User recordToUpdate;

        if (possibleRecordToUpdate.isEmpty()) {
            recordToUpdate = new User();
        } else {
            recordToUpdate = possibleRecordToUpdate.get();
        }

        recordToUpdate.setName(user.getName());
        recordToUpdate.setEmail(user.getEmail());

        return userRepository.save(recordToUpdate);
    }

    /**
     * Deletes the user associated with the provided Id
     * @param id The Id whose associated User will be deleted
     */
    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
