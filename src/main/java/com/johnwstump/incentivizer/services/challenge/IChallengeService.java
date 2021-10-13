package com.johnwstump.incentivizer.services.challenge;

import com.johnwstump.incentivizer.model.challenge.Challenge;
import com.johnwstump.incentivizer.model.challengeentry.ChallengeEntry;
import com.johnwstump.incentivizer.model.user.impl.User;

import java.util.List;
import java.util.Optional;

public interface IChallengeService {
    Challenge createChallenge(Challenge challengeInput, User currentUser);

    List<Challenge> findChallengesForUser(long userId);

    Optional<Challenge> findChallenge(long challengeId);

    ChallengeEntry createChallengeEntry(ChallengeEntry newChallengeEntry);

    Optional<ChallengeEntry> getChallengeEntry(long challengeEntryId);

    void acknowledgeSuccess(long challengeId) throws IllegalArgumentException;
}
