package com.johnwstump.incentivizer.model.challenge.dto;

import com.johnwstump.incentivizer.model.challengeentry.dto.ChallengeEntryDisplay;
import com.johnwstump.incentivizer.model.goal.dto.GoalDisplay;
import com.johnwstump.incentivizer.model.reward.dto.RewardDisplay;
import lombok.Data;

import java.util.List;

/**
 * The Display object for a {@link com.johnwstump.incentivizer.model.challenge.Challenge Challenge}.
 * <p>
 * Contains the fields needed to display and format the information for a
 * single Challenge.
 */
public @Data
class ChallengeDisplay {
    private String name;
    private long id;
    private long ownerId;
    private GoalDisplay goal;
    private List<ChallengeEntryDisplay> entries;
    private RewardDisplay reward;

    private int numEntries;
    private double numPoints;

    private boolean completed;
    private boolean failed;

    private boolean successAcknowledged;
}
