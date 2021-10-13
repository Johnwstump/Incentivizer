package com.johnwstump.incentivizer.model.reward.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public @Data
class RewardInput {

    private String entryComparatorCode;
    private int entryTarget;
    private String pointComparatorCode;
    private int pointTarget;
    private String rewardDescription;
}
