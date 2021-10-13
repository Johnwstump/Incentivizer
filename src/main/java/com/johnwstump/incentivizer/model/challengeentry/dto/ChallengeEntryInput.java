package com.johnwstump.incentivizer.model.challengeentry.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Input object for a {@link com.johnwstump.incentivizer.model.challengeentry.ChallengeEntry Challenge Entry}.
 * <p>
 * Contains the fields needed to create a new Challenge Entry.
 */
@NoArgsConstructor
public @Data
class ChallengeEntryInput {
    long challengeId;
    double value;
}
