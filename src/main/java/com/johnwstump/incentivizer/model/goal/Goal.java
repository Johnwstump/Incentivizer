package com.johnwstump.incentivizer.model.goal;

import com.johnwstump.incentivizer.model.challengeentry.ChallengeEntry;
import com.johnwstump.incentivizer.model.comparator.Comparator;
import com.johnwstump.incentivizer.model.metric.Metric;
import com.johnwstump.incentivizer.model.pointstrategy.PointStrategy;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@Table(name = "goal")
public @Data
class Goal {

    public Goal(Goal goal) {
        setId(goal.getId());
        PointStrategy strategy = PointStrategy.buildPointStrategy(goal.getPointStrategyCode());
        setPointStrategy(strategy);
        setMetric(goal.metric);
        setPointQty(goal.getPointQty());
        setPointIncrement(goal.getPointIncrement());
        setComparator(goal.getComparator());
        setGoalTarget(goal.getGoalTarget());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(20)")
    private PointStrategy pointStrategy;

    private double pointQty;

    private double pointIncrement;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "metricId", referencedColumnName = "id")
    private Metric metric;

    @Column(columnDefinition = "VARCHAR(5)")
    private Comparator comparator;

    private double goalTarget;

    String getPointStrategyCode() {
        if (pointStrategy == null) {
            return "";
        }

        return pointStrategy.getStrategyCode();
    }

    public long getMetricId() {
        return metric.getId();
    }

    public double determinePoints(ChallengeEntry entry) {
        return (Math.max(pointStrategy.determinePoints(entry.getValue(), goalTarget, comparator), 0) / pointIncrement) * pointQty;
    }
}
