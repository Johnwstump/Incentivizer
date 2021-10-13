package com.johnwstump.incentivizer.dao;

import com.johnwstump.incentivizer.model.challengeentry.ChallengeEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeEntryRepository extends CrudRepository<ChallengeEntry, Long> {
}
