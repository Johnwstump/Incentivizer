package com.johnwstump.incentivizer.model.goal;

import com.johnwstump.incentivizer.model.comparator.Comparator;
import com.johnwstump.incentivizer.model.comparator.ComparatorMapper;
import com.johnwstump.incentivizer.model.comparator.dto.ComparatorDisplay;
import com.johnwstump.incentivizer.model.goal.dto.GoalDisplay;
import com.johnwstump.incentivizer.model.goal.dto.GoalInput;
import com.johnwstump.incentivizer.model.metric.Metric;
import com.johnwstump.incentivizer.model.metric.MetricMapper;
import com.johnwstump.incentivizer.model.metric.dto.MetricDisplay;
import com.johnwstump.incentivizer.model.pointstrategy.PointStrategy;
import com.johnwstump.incentivizer.model.pointstrategy.PointStrategyMapper;
import com.johnwstump.incentivizer.model.pointstrategy.impl.PointStrategyDisplay;
import com.johnwstump.incentivizer.services.metric.impl.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GoalMapper {

    private final MetricService metricService;
    private final ComparatorMapper comparatorMapper;
    private final MetricMapper metricMapper;
    private final PointStrategyMapper pointStrategyMapper;

    @Autowired
    public GoalMapper(MetricService metricService, ComparatorMapper comparatorMapper,
                      MetricMapper metricMapper, PointStrategyMapper pointStrategyMapper) {
        this.metricService = metricService;
        this.comparatorMapper = comparatorMapper;
        this.metricMapper = metricMapper;
        this.pointStrategyMapper = pointStrategyMapper;
    }

    public GoalDisplay toDisplay(Goal goal) {
        GoalDisplay goalDisplay = new GoalDisplay();
        PointStrategyDisplay pointStrategyDisplay = pointStrategyMapper.toDisplay(goal.getPointStrategy());
        goalDisplay.setPointStrategy(pointStrategyDisplay);
        goalDisplay.setPointQty(goal.getPointQty());
        goalDisplay.setPointIncrement(goal.getPointIncrement());
        MetricDisplay metricDisplay = metricMapper.toDisplay(goal.getMetric());
        goalDisplay.setMetric(metricDisplay);
        ComparatorDisplay goalComparator = comparatorMapper.toDisplay(goal.getComparator());
        goalDisplay.setGoalComparator(goalComparator);
        goalDisplay.setGoalTarget(goal.getGoalTarget());
        return goalDisplay;
    }

    public List<GoalDisplay> toDisplay(List<Goal> goals) {
        List<GoalDisplay> goalDisplays = new ArrayList<>();

        for (Goal goal : goals) {
            GoalDisplay goalDisplay = toDisplay(goal);
            goalDisplays.add(goalDisplay);
        }

        return goalDisplays;
    }

    public List<Goal> toGoal(List<GoalInput> goalInputs) {
        List<Goal> goals = new ArrayList<>();

        for (GoalInput goalInput : goalInputs) {
            Goal goal = toGoal(goalInput);
            goals.add(goal);
        }

        return goals;
    }

    public Goal toGoal(GoalInput inputGoal) {
        Goal goal = new Goal();

        PointStrategy strategy = PointStrategy.buildPointStrategy(inputGoal.getPointStrategyCode());
        goal.setPointStrategy(strategy);

        Optional<Metric> possibleMetric = metricService.getMetric(inputGoal.getMetricId());

        if (possibleMetric.isEmpty()) {
            String message = String.format("Invalid goal metric id %d", inputGoal.getMetricId());
            throw new IllegalStateException(message);
        }

        goal.setMetric(possibleMetric.get());
        goal.setPointQty(inputGoal.getPointQty());
        goal.setPointIncrement(inputGoal.getPointIncrement());
        Comparator goalComparator = Comparator.getComparatorForSymbol(inputGoal.getGoalComparator());
        goal.setComparator(goalComparator);
        goal.setGoalTarget(inputGoal.getGoalTarget());

        return goal;
    }
}
