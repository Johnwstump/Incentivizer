package com.johnwstump.incentivizer.model.challenge;

import com.johnwstump.incentivizer.model.challenge.dto.ChallengeDisplay;
import com.johnwstump.incentivizer.model.challenge.dto.ChallengeInput;
import com.johnwstump.incentivizer.model.goal.Goal;
import com.johnwstump.incentivizer.model.goal.GoalMapper;
import com.johnwstump.incentivizer.model.reward.Reward;
import com.johnwstump.incentivizer.model.reward.RewardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A Mapper/Factory used to translate {@link com.johnwstump.incentivizer.model.challenge.Challenge Challenges} to
 * and from the corresponding display objects.
 */
@Component
public class ChallengeMapper {

    private GoalMapper goalMapper;
    private RewardMapper rewardMapper;

    @Autowired
    public ChallengeMapper(GoalMapper goalMapper, RewardMapper rewardMapper) {
        this.goalMapper = goalMapper;
        this.rewardMapper = rewardMapper;
    }

    /**
     * Uses a list of {@link com.johnwstump.incentivizer.model.challenge.Challenge Challenges} to generate
     * a list of {@link ChallengeDisplay Challenge Displays}. One display will be generated for each
     * Challenge provided.
     *
     * @param challenges The List of challenges to be converted
     * @return A list of Challenge Displays derived from the challenges provided. May be empty, but not null.
     * @see ChallengeMapper#toDisplay(Challenge)
     */
    public List<ChallengeDisplay> toDisplay(List<Challenge> challenges) {
        List<ChallengeDisplay> challengeDisplays = new ArrayList<>();

        if (challenges == null) {
            return challengeDisplays;
        }

        for (Challenge challenge : challenges) {
            ChallengeDisplay challengeDisplay = toDisplay(challenge);
            challengeDisplays.add(challengeDisplay);
        }

        return challengeDisplays;
    }

    /**
     * <p>
     * Uses the provided {@link com.johnwstump.incentivizer.model.challenge.Challenge Challenge} to generate an
     * instance of {@link ChallengeDisplay Challenge Display}.
     * </p>
     * The Challenge provided will not be modified. <br>
     * The Display object generated shares no mutable references with the provided challenge. <br>
     *
     * @param challenge The challenge whose corresponding display object will be returned.
     * @return An instance of ChallengeDisplay corresponding to the provided challenge. If the provided challenge was
     * null, the returned value is null.
     */
    public ChallengeDisplay toDisplay(Challenge challenge) {
        if (challenge == null) {
            return null;
        }

        ChallengeDisplay challengeDisplay = new ChallengeDisplay();

        challengeDisplay.setName(challenge.getName());
        challengeDisplay.setId(challenge.getId());
        challengeDisplay.setOwnerId(challenge.getOwnerId());
        challengeDisplay.setGoal(goalMapper.toDisplay(challenge.getGoal()));
        challengeDisplay.setReward(rewardMapper.toDisplay(challenge.getReward()));

        challengeDisplay.setNumEntries(challenge.getNumEntries());
        challengeDisplay.setNumPoints(challenge.getNumPoints());

        challengeDisplay.setSuccessAcknowledged(challenge.getSuccessAcknowledged() != null);
        challengeDisplay.setCompleted(challenge.getCompleted());
        challengeDisplay.setFailed(challenge.getFailed());

        return challengeDisplay;
    }

    /**
     * <p>
     * Uses the provided {@link ChallengeInput Challenge Input} to generate an
     * instance of {@link com.johnwstump.incentivizer.model.challenge.Challenge Challenge}.
     * </p>
     * The Challenge Input provided will not be modified. <br>
     * The Challenge object generated shares no mutable references with the provided input. <br>
     *
     * @param challengeInput The challenge input which will be used to generate an instance of Challenge. Cannot be null.
     * @return A Challenge generated from the provided input.
     */
    public Challenge toChallenge(ChallengeInput challengeInput) {
        if (challengeInput == null) {
            throw new IllegalArgumentException("Challenge input cannot be null.");
        }

        Challenge challenge = new Challenge();
        challenge.setName(challengeInput.getName());
        Reward reward = rewardMapper.toReward(challengeInput.getReward());

        challenge.setReward(reward);

        Goal goal = goalMapper.toGoal(challengeInput.getGoal());

        challenge.setGoal(goal);

        return challenge;
    }
}
