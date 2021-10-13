package com.johnwstump.incentivizer.services.goal.impl;

import com.johnwstump.incentivizer.dao.GoalRepository;
import com.johnwstump.incentivizer.model.goal.Goal;
import com.johnwstump.incentivizer.services.goal.IGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoalService implements IGoalService {
    private final GoalRepository goalRepository;

    @Autowired
    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @Override
    public Goal createGoal(Goal inputGoal) {
        Goal goal = new Goal(inputGoal);
        return goalRepository.save(goal);
    }

}
