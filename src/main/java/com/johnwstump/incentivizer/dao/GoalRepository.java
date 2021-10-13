package com.johnwstump.incentivizer.dao;

import com.johnwstump.incentivizer.model.goal.Goal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends CrudRepository<Goal, Long> {
}
