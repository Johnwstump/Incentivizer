package com.johnwstump.incentivizer.services.reward.impl;

import com.johnwstump.incentivizer.dao.RewardRepository;
import com.johnwstump.incentivizer.model.reward.Reward;
import com.johnwstump.incentivizer.services.reward.IRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RewardService implements IRewardService {
    private final RewardRepository rewardRepository;

    @Autowired
    public RewardService(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    @Override
    public Reward createReward(Reward reward) {
        return rewardRepository.save(reward);
    }
}
