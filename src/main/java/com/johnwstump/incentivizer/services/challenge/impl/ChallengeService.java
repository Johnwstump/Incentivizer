package com.johnwstump.incentivizer.services.challenge.impl;

import com.johnwstump.incentivizer.dao.ChallengeEntryRepository;
import com.johnwstump.incentivizer.dao.ChallengeRepository;
import com.johnwstump.incentivizer.model.challenge.Challenge;
import com.johnwstump.incentivizer.model.challengeentry.ChallengeEntry;
import com.johnwstump.incentivizer.model.user.impl.User;
import com.johnwstump.incentivizer.services.challenge.IChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengeService implements IChallengeService {
    private ChallengeRepository challengeRepository;
    private ChallengeEntryRepository challengeEntryRepository;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository,
                            ChallengeEntryRepository challengeEntryRepository) {
        this.challengeRepository = challengeRepository;
        this.challengeEntryRepository = challengeEntryRepository;
    }

    @Override
    public Challenge createChallenge(Challenge challengeInput, User currentUser) {
        return challengeRepository.save(challengeInput);
    }

    @Override
    public List<Challenge> findChallengesForUser(long userId) {
        User user = new User();
        user.setId(userId);
        return challengeRepository.findAllByOwner(user);
    }

    @Override
    public Optional<Challenge> findChallenge(long challengeId) {
        return challengeRepository.findById(challengeId);
    }

    @Override
    public ChallengeEntry createChallengeEntry(ChallengeEntry newChallengeEntry) {
        return challengeEntryRepository.save(newChallengeEntry);
    }

    @Override
    public Optional<ChallengeEntry> getChallengeEntry(long challengeEntryId) {
        return challengeEntryRepository.findById(challengeEntryId);
    }

    @Override
    public void acknowledgeSuccess(long challengeId) throws IllegalArgumentException {
        Optional<Challenge> possibleChallenge = findChallenge(challengeId);
        if (possibleChallenge.isEmpty()) {
            throw new IllegalArgumentException("Challenge with id " + challengeId + " does not exist.");
        }

        possibleChallenge.get().setSuccessAcknowledged(Calendar.getInstance());

        this.challengeRepository.save(possibleChallenge.get());
    }
}
