package com.johnwstump.incentivizer.model.challengeentry;

import com.johnwstump.incentivizer.model.challengeentry.dto.ChallengeEntryDisplay;
import com.johnwstump.incentivizer.model.challengeentry.dto.ChallengeEntryInput;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class ChallengeEntryMapper {

    public ChallengeEntry toChallengeEntry(ChallengeEntryInput challengeEntryInput) {
        ChallengeEntry entry = new ChallengeEntry();
        entry.setChallengeId(challengeEntryInput.getChallengeId());
        entry.setValue(challengeEntryInput.getValue());
        return entry;
    }

    public ChallengeEntryDisplay toDisplay(ChallengeEntry challengeEntry) {
        ChallengeEntryDisplay displayEntry = new ChallengeEntryDisplay();
        displayEntry.setValue(challengeEntry.getValue());

        ZoneId zoneId = challengeEntry.getCreated().getTimeZone().toZoneId();
        Instant createdInstant = challengeEntry.getCreated().toInstant();

        LocalDateTime createdDateTime = LocalDateTime.ofInstant(createdInstant, zoneId);

        displayEntry.setCreatedDateTime(createdDateTime);

        displayEntry.setPoints(challengeEntry.getPoints());
        return displayEntry;
    }
}
