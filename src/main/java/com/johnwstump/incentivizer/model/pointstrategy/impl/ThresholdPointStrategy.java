package com.johnwstump.incentivizer.model.pointstrategy.impl;

import com.johnwstump.incentivizer.model.comparator.Comparator;
import com.johnwstump.incentivizer.model.pointstrategy.PointStrategy;

public class ThresholdPointStrategy extends PointStrategy {
    public static final String STRATEGY_CODE = "THRESHOLD";

    public double determinePoints(double entryValue, double target, Comparator comparator) {
        return comparator.compare(entryValue, target) ? 1 : 0;
    }

    @Override
    public String getStrategyCode() {
        return STRATEGY_CODE;
    }
}
