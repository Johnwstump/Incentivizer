package com.johnwstump.incentivizer.model.goal.dto;

import com.johnwstump.incentivizer.model.comparator.dto.ComparatorDisplay;
import com.johnwstump.incentivizer.model.metric.dto.MetricDisplay;
import com.johnwstump.incentivizer.model.pointstrategy.impl.PointStrategyDisplay;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public @Data
class GoalDisplay {
    private PointStrategyDisplay pointStrategy;
    private double pointQty;
    private double pointIncrement;
    private MetricDisplay metric;
    private ComparatorDisplay goalComparator;
    private double goalTarget;

    public String getGoalComparator() {
        return this.goalComparator == null ? "" : this.goalComparator.getSymbol();
    }

    public long getMetricId() {
        return this.metric == null ? 0 : this.metric.getId();
    }

    public String getMetricName() {
        return this.metric == null ? "" : this.metric.getViewName();
    }

    public String getPointStrategyCode() {
        return this.pointStrategy == null ? "" : this.pointStrategy.getStrategyCode();
    }

    public String getPointStrategyDescription() {
        return this.pointStrategy == null ? "" : this.pointStrategy.getDescription();
    }
}
