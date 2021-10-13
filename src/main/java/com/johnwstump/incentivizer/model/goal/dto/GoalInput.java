package com.johnwstump.incentivizer.model.goal.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public @Data
class GoalInput {
    private String pointStrategyCode;
    private int metricId;
    private double pointQty;
    private double pointIncrement;
    private String goalComparator;
    private int goalTarget;
}
