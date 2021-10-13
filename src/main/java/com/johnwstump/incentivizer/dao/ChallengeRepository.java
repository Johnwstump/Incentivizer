package com.johnwstump.incentivizer.dao;

import com.johnwstump.incentivizer.model.challenge.Challenge;
import com.johnwstump.incentivizer.model.user.impl.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends CrudRepository<Challenge, Long> {
    /**
     * Finds all of the challenges for the indicated owner.
     *
     * @param owner The user whose challenges will be returned
     * @return All of the challenges for the indicated user.
     */
    List<Challenge> findAllByOwner(User owner);
}
