package com.johnwstump.incentivizer.services.impl;

import com.johnwstump.incentivizer.dao.UserRepository;
import com.johnwstump.incentivizer.model.user.IUser;
import com.johnwstump.incentivizer.model.user.impl.UserDTO;
import com.johnwstump.incentivizer.model.user.impl.UserRecord;
import com.johnwstump.incentivizer.services.IUserService;
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
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserRecord registerNewUser(@Valid UserDTO newUser) throws UserAlreadyExistsException {
        if (userWithNameOrEmailExists(newUser.getName(), newUser.getEmail())){
            throw new UserAlreadyExistsException("A user already exists with that name/email address.");
        }

        UserRecord record = new UserRecord();
        record.setName(newUser.getName());
        record.setEmail(newUser.getEmail());
        record.setEncryptedPassword(passwordEncoder.encode(newUser.getPlainPassword()));
        return userRepository.save(record);
    }

    private boolean userWithNameOrEmailExists(String email, String name){
        List<UserRecord> usersWithSameEmail = this.userRepository.findByEmailOrName(email, name);

        return !usersWithSameEmail.isEmpty();
    }

    @Override
    public List<UserRecord> getAllUsers() {
        List<UserRecord> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public Optional<UserRecord> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserRecord save(long id, IUser user) {
        Optional<UserRecord> possibleRecordToUpdate = findById(id);

        UserRecord recordToUpdate;

        if (possibleRecordToUpdate.isEmpty()){
            recordToUpdate = new UserRecord();
        }
        else {
            recordToUpdate = possibleRecordToUpdate.get();
        }

        recordToUpdate.setName(user.getName());
        recordToUpdate.setEmail(user.getEmail());

        return userRepository.save(recordToUpdate);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
