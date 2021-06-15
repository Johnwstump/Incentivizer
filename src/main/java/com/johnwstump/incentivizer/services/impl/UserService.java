package com.johnwstump.incentivizer.services.impl;

import com.johnwstump.incentivizer.dao.UserRepository;
import com.johnwstump.incentivizer.model.IUser;
import com.johnwstump.incentivizer.model.impl.UserRecord;
import com.johnwstump.incentivizer.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private UserRepository userRepository;

    @Autowired
    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
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
    public UserRecord save(IUser user) {
        UserRecord recordToInsert = new UserRecord(user.getName(), user.getEmail());
        return userRepository.save(recordToInsert);
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
