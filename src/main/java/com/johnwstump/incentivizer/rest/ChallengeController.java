package com.johnwstump.incentivizer.rest;

import com.johnwstump.incentivizer.model.challenge.Challenge;
import com.johnwstump.incentivizer.model.challenge.ChallengeMapper;
import com.johnwstump.incentivizer.model.challenge.dto.ChallengeDisplay;
import com.johnwstump.incentivizer.model.challenge.dto.ChallengeInput;
import com.johnwstump.incentivizer.model.challengeentry.ChallengeEntry;
import com.johnwstump.incentivizer.model.challengeentry.ChallengeEntryMapper;
import com.johnwstump.incentivizer.model.challengeentry.dto.ChallengeEntryDisplay;
import com.johnwstump.incentivizer.model.challengeentry.dto.ChallengeEntryInput;
import com.johnwstump.incentivizer.model.comparator.Comparator;
import com.johnwstump.incentivizer.model.comparator.ComparatorMapper;
import com.johnwstump.incentivizer.model.comparator.dto.ComparatorDisplay;
import com.johnwstump.incentivizer.model.pointstrategy.impl.PointStrategyDisplay;
import com.johnwstump.incentivizer.model.user.impl.User;
import com.johnwstump.incentivizer.services.challenge.IChallengeService;
import com.johnwstump.incentivizer.services.comparator.IComparatorService;
import com.johnwstump.incentivizer.services.user.IUserService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for exposing endpoints related to manipulating Challenges and associated
 * entities.
 */
@org.springframework.web.bind.annotation.RestController
@CommonsLog
@RequestMapping("/api")
public class ChallengeController extends AbstractRestController {
    private final ChallengeEntryMapper challengeEntryMapper;
    private final IComparatorService comparatorService;
    private final IChallengeService challengeService;
    private final IUserService userService;

    private final ComparatorMapper comparatorMapper;
    private final ChallengeMapper challengeMapper;

    @Autowired
    public ChallengeController(IComparatorService comparatorService,
                               IChallengeService challengeService, IUserService userService,
                               ChallengeMapper challengeMapper, ComparatorMapper comparatorMapper,
                               ChallengeEntryMapper challengeEntryMapper) {
        this.comparatorService = comparatorService;
        this.challengeService = challengeService;
        this.userService = userService;
        this.challengeMapper = challengeMapper;
        this.comparatorMapper = comparatorMapper;
        this.challengeEntryMapper = challengeEntryMapper;
    }

    @GetMapping("/pointStrategy/")
    public ResponseEntity<List<PointStrategyDisplay>> getPointStrategies() {
        List<PointStrategyDisplay> strategies = PointStrategyDisplay.getPointStrategyDisplays();

        return new ResponseEntity<>(strategies, HttpStatus.OK);
    }

    @GetMapping("/comparator/")
    public ResponseEntity<List<ComparatorDisplay>> getAllComparators() {
        List<ComparatorDisplay> comparatorDisplays = new ArrayList<>();

        for (Comparator comparator : comparatorService.getAllComparators()) {
            comparatorDisplays.add(comparatorMapper.toDisplay(comparator));
        }

        return new ResponseEntity<>(comparatorDisplays, HttpStatus.OK);
    }

    @GetMapping("/challenge/forUser/{userId}")
    public ResponseEntity<List<ChallengeDisplay>> getChallengesForUser(@PathVariable() String userId) {
        try {
            long userIdNumeric = Long.parseLong(userId);

            if (getCurrentHttpRequest().isEmpty()) {
                throw new IllegalStateException("Current HTTP Request cannot be determined.");
            }

            long authorizedId = currentUserUtil.getCurrentUserId(getCurrentHttpRequest().get());

            if (userIdNumeric != authorizedId) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can only retrieve their own challenges.");
            }

            List<Challenge> challenges = challengeService.findChallengesForUser(userIdNumeric);

            List<ChallengeDisplay> challengeInputs = challengeMapper.toDisplay(challenges);

            return new ResponseEntity<>(challengeInputs, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        }
    }

    @GetMapping("/challenge/{challengeId}")
    public ResponseEntity<ChallengeDisplay> getChallenge(@PathVariable() String challengeId) {
        try {
            long challengeIdNumeric = Long.parseLong(challengeId);

            if (getCurrentHttpRequest().isEmpty()) {
                throw new IllegalStateException("Current HTTP Request cannot be determined.");
            }

            Optional<Challenge> possibleChallenge = challengeService.findChallenge(challengeIdNumeric);

            if (possibleChallenge.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge not found.");
            }

            long authorizedId = currentUserUtil.getCurrentUserId(getCurrentHttpRequest().get());

            if (possibleChallenge.get().getOwnerId() != authorizedId) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User can only retrieve their own challenges.");
            }

            ChallengeDisplay challengeDisplay = challengeMapper.toDisplay(possibleChallenge.get());
            return new ResponseEntity<>(challengeDisplay, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        }
    }

    @PostMapping("/challenge/")
    public ResponseEntity<ChallengeInput> createChallenge(@RequestBody ChallengeInput challengeInput) {
        try {
            Optional<User> user = userService.findById(challengeInput.getOwnerId());

            if (user.isEmpty()) {
                log.warn("Created challenge with invalid owner " + user);
                throw new IllegalArgumentException("Invalid challenge owner.");
            }

            Challenge newChallenge = challengeMapper.toChallenge(challengeInput);

            newChallenge.setOwner(user.get());

            Challenge createdChallenge = challengeService.createChallenge(newChallenge, user.get());

            URI getLocation = ServletUriComponentsBuilder.fromCurrentRequest().path(
                    "/{challengeId}").buildAndExpand(createdChallenge.getId()).toUri();

            return ResponseEntity.created(getLocation).build();
        } catch (Exception ex) {
            log.error(ex);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        }
    }

    @PostMapping("/challenge/entry/")
    public ResponseEntity<ChallengeEntryDisplay> createChallengeEntry(@RequestBody ChallengeEntryInput challengeEntryInput) {
        try {
            ChallengeEntry newChallengeEntry = challengeEntryMapper.toChallengeEntry(challengeEntryInput);

            newChallengeEntry = challengeService.createChallengeEntry(newChallengeEntry);

            ChallengeEntryDisplay challengeEntryDisplay = challengeEntryMapper.toDisplay(newChallengeEntry);

            URI getLocation = ServletUriComponentsBuilder.fromCurrentRequest().path(
                    "/{entryId}").buildAndExpand(newChallengeEntry.getId()).toUri();

            return ResponseEntity.created(getLocation).body(challengeEntryDisplay);
        } catch (Exception ex) {
            log.error(ex);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        }
    }

    @GetMapping("/challenge/entry/{challengeEntryId}")
    public ResponseEntity<ChallengeEntryDisplay> getChallengeEntry(@PathVariable String challengeEntryId) {
        try {
            long challengeEntryIdNumeric = Long.parseLong(challengeEntryId);

            Optional<ChallengeEntry> challengeEntry = challengeService.getChallengeEntry(challengeEntryIdNumeric);

            if (challengeEntry.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge Entry Not Found");
            }

            ChallengeEntryDisplay entryDisplay = challengeEntryMapper.toDisplay(challengeEntry.get());

            return new ResponseEntity<>(entryDisplay, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        }
    }

    @GetMapping("/challenge/acknowledge/{challengeId}")
    public ResponseEntity<Boolean> acknowledgeChallengeSuccess(@PathVariable String challengeId) {
        try {
            long challengeIdNumeric = Long.parseLong(challengeId);

            this.challengeService.acknowledgeSuccess(challengeIdNumeric);

            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception ex) {
            log.error(ex);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        }
    }

}
