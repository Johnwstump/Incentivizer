package com.johnwstump.incentivizer.dao;

import com.johnwstump.incentivizer.model.user.impl.UserRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserRecord, Long> {
    List<UserRecord> findByEmail(String email);
    List<UserRecord> findByEmailOrName(String email, String name);
}
