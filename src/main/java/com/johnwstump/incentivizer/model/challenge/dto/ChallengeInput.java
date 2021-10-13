package com.johnwstump.incentivizer.model.challenge.dto;

import com.johnwstump.incentivizer.model.goal.dto.GoalInput;
import com.johnwstump.incentivizer.model.reward.dto.RewardInput;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Input object for a {@link com.johnwstump.incentivizer.model.challenge.Challenge Challenge}.
 * <p>
 * Contains the fields needed to create a new Challenge.
 */
@NoArgsConstructor
public @Data
class ChallengeInput {
    private String name;
    private long ownerId;
    private GoalInput goal;
    private RewardInput reward;
}
