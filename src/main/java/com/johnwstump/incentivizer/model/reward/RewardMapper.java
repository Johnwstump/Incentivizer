package com.johnwstump.incentivizer.model.reward;

import com.johnwstump.incentivizer.model.comparator.Comparator;
import com.johnwstump.incentivizer.model.comparator.ComparatorMapper;
import com.johnwstump.incentivizer.model.comparator.dto.ComparatorDisplay;
import com.johnwstump.incentivizer.model.reward.dto.RewardDisplay;
import com.johnwstump.incentivizer.model.reward.dto.RewardInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RewardMapper {

    private final ComparatorMapper comparatorMapper;

    @Autowired
    public RewardMapper(ComparatorMapper comparatorMapper) {
        this.comparatorMapper = comparatorMapper;
    }

    public RewardDisplay toDisplay(Reward reward) {
        RewardDisplay rewardDisplay = new RewardDisplay();

        ComparatorDisplay entryComparatorDisplay = comparatorMapper.toDisplay(reward.getEntryComparator());
        rewardDisplay.setEntryComparator(entryComparatorDisplay);
        rewardDisplay.setEntryTarget(reward.getEntryTarget());

        ComparatorDisplay pointComparatorDisplay = comparatorMapper.toDisplay(reward.getPointComparator());
        rewardDisplay.setPointComparator(pointComparatorDisplay);
        rewardDisplay.setPointTarget(reward.getPointTarget());

        rewardDisplay.setRewardDescription(reward.getReward());

        rewardDisplay.setSatisfiesEntryRequirements(reward.satisfiesEntryRequirements());
        rewardDisplay.setSatisfiesPointRequirements(reward.satisfiesPointRequirements());

        rewardDisplay.setFailed(reward.failed());

        return rewardDisplay;
    }

    public Reward toReward(RewardInput rewardInput) {
        Reward reward = new Reward();

        Comparator entryComparator = Comparator.getComparatorForSymbol(rewardInput.getEntryComparatorCode());
        reward.setEntryComparator(entryComparator);
        reward.setEntryTarget(rewardInput.getEntryTarget());
        Comparator pointComparator = Comparator.getComparatorForSymbol(rewardInput.getPointComparatorCode());
        reward.setPointComparator(pointComparator);
        reward.setPointTarget(rewardInput.getPointTarget());
        reward.setReward(rewardInput.getRewardDescription());

        return reward;
    }
}
