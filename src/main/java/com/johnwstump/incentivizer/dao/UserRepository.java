package com.johnwstump.incentivizer.dao;

import com.johnwstump.incentivizer.model.impl.UserRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserRecord, Long> {}
