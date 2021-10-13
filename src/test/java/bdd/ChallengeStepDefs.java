package bdd;

import com.johnwstump.incentivizer.dao.MetricRepository;
import com.johnwstump.incentivizer.model.challenge.Challenge;
import com.johnwstump.incentivizer.model.challengeentry.ChallengeEntry;
import com.johnwstump.incentivizer.model.goal.Goal;
import com.johnwstump.incentivizer.model.goal.GoalMapper;
import com.johnwstump.incentivizer.model.goal.dto.GoalInput;
import com.johnwstump.incentivizer.model.metric.Metric;
import com.johnwstump.incentivizer.model.pointstrategy.impl.IncrementalPointStrategy;
import com.johnwstump.incentivizer.model.pointstrategy.impl.ThresholdPointStrategy;
import com.johnwstump.incentivizer.model.reward.Reward;
import com.johnwstump.incentivizer.model.reward.RewardMapper;
import com.johnwstump.incentivizer.model.reward.dto.RewardInput;
import com.johnwstump.incentivizer.services.challenge.impl.ChallengeService;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChallengeStepDefs {

    private static final int DEFAULT_METRIC_ID = 1;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private GoalMapper goalMapper;

    @Autowired
    private RewardMapper rewardMapper;

    private Challenge currentChallenge;

    @Before(value = "@Challenges")
    public void beforeChallenges() {
        // Create default metric
        Metric metric = new Metric();
        metric.setDefault(false);
        metric.setId(DEFAULT_METRIC_ID);
        metric.setName("MINUTE");
        metricRepository.save(metric);
    }

    @Given("I have a challenge")
    public void ihaveAChallenge() {
        Challenge challenge = new Challenge();

        challenge.setName("Test Challenge");

        this.currentChallenge = challenge;
    }


    @Given("the challenge has a goal where {int} points are earned whenever an entry is {string} {int}")
    public void theChallengeHasAThresholdGoal(int quantity, String comparator, int threshold) {
        GoalInput input = new GoalInput();
        input.setMetricId(DEFAULT_METRIC_ID);
        input.setPointQty(quantity);
        input.setGoalComparator(comparator);
        input.setPointIncrement(1);
        input.setGoalTarget(threshold);
        input.setPointStrategyCode(ThresholdPointStrategy.STRATEGY_CODE);

        Goal goal = goalMapper.toGoal(input);

        this.currentChallenge.setGoal(goal);
    }

    @Given("the challenge has a goal where {int} points are earned for every {int} {string} {int}")
    public void theChallengeHasAnIncrementGoal(int quantity, int increment, String comparator, int threshold) {
        GoalInput input = new GoalInput();
        input.setMetricId(DEFAULT_METRIC_ID);
        input.setPointQty(quantity);
        input.setGoalComparator(comparator);
        input.setPointIncrement(increment);
        input.setGoalTarget(threshold);
        input.setPointStrategyCode(IncrementalPointStrategy.STRATEGY_CODE);

        Goal goal = goalMapper.toGoal(input);

        this.currentChallenge.setGoal(goal);
    }

    @Given("I create an entry with value {int}")
    public void ihaveAnEntryWithValue(int value) {
        ChallengeEntry entry = new ChallengeEntry();
        entry.setChallengeId(this.currentChallenge.getId());
        entry.setValue(value);

        this.currentChallenge.getEntries().add(entry);
    }

    @Then("I should receive {double} points")
    public void iShouldReceivePointsPoints(double points) {
        assertThat(this.currentChallenge.getNumPoints()).as("Points should match.").isEqualTo(points);
    }

    @Given("the challenge has a reward triggered when there are {string} {int} posts and {string} {int} points")
    public void theChallengeHasAReward(String postComparator, int postTarget, String pointComparator, int pointTarget) {
        RewardInput rewardInput = new RewardInput();

        rewardInput.setEntryComparatorCode(postComparator);
        rewardInput.setEntryTarget(postTarget);

        rewardInput.setPointComparatorCode(pointComparator);
        rewardInput.setPointTarget(pointTarget);

        Reward reward = rewardMapper.toReward(rewardInput);

        this.currentChallenge.setReward(reward);
    }

    @When("the challenge has {int} posts and {double} points")
    public void haveXPostsAndYPoints(int postQuantity, double pointTarget) {
        Challenge mockChallenge = mock(Challenge.class);

        when(mockChallenge.getNumEntries()).thenReturn(postQuantity);
        when(mockChallenge.getNumPoints()).thenReturn(pointTarget);

        this.currentChallenge.getReward().setChallenge(mockChallenge);
    }

    @Then("the challenge should show as succeeded")
    public void theChallengeShouldShowAsSucceeded() {
        assertThat(this.currentChallenge.getCompleted()).as("Challenge should show as complete.").isTrue();
    }

    @Then("the challenge should show as failed")
    public void theChallengeShouldShowAsFailed() {
        assertThat(this.currentChallenge.getCompleted()).as("Challenge should show as failed.").isFalse();
        assertThat(this.currentChallenge.getFailed()).as("Challenge should show as failed.").isTrue();
    }

    @Then("the challenge should show as neither succeeded nor failed")
    public void theChallengeShouldShowAsNeitherSucceededNorFailed() {
        assertThat(this.currentChallenge.getCompleted()).as("Challenge should not show as completed.").isFalse();
        assertThat(this.currentChallenge.getFailed()).as("Challenge should not show as failed.").isFalse();
    }
}
