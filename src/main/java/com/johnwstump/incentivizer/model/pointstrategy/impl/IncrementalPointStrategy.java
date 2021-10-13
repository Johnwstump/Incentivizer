package com.johnwstump.incentivizer.model.pointstrategy.impl;

import com.johnwstump.incentivizer.model.comparator.Comparator;
import com.johnwstump.incentivizer.model.pointstrategy.PointStrategy;

public class IncrementalPointStrategy extends PointStrategy {
    public static final String STRATEGY_CODE = "INCREMENTAL";

    public double determinePoints(double entryValue, double target, Comparator comparator) {
        return comparator.getPoints(entryValue, target);
    }

    @Override
    public String getStrategyCode() {
        return STRATEGY_CODE;
    }
}
