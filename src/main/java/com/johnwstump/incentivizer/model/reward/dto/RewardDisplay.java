package com.johnwstump.incentivizer.model.reward.dto;

import com.johnwstump.incentivizer.model.comparator.dto.ComparatorDisplay;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public @Data
class RewardDisplay {

    private ComparatorDisplay entryComparator;
    private int entryTarget;
    private ComparatorDisplay pointComparator;
    private int pointTarget;
    private String rewardDescription;

    private boolean satisfiesEntryRequirements;
    private boolean satisfiesPointRequirements;

    public boolean failed;

    public String getEntryComparatorCode() {
        return entryComparator.getSymbol();
    }

    public String getPointComparatorCode() {
        return pointComparator.getSymbol();
    }
}
