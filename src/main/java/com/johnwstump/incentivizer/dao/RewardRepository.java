package com.johnwstump.incentivizer.dao;

import com.johnwstump.incentivizer.model.reward.Reward;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends CrudRepository<Reward, Long> {
}
