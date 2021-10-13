package com.johnwstump.incentivizer.model.challengeentry.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The Display object for a {@link com.johnwstump.incentivizer.model.challengeentry.ChallengeEntry Challenge Entry}.
 * <p>
 * Contains the fields needed to display and format the information for a
 * single Challenge Entry.
 */
@NoArgsConstructor
public @Data
class ChallengeEntryDisplay {
    public double value;
    public LocalDateTime createdDateTime;

    public double points;
}
