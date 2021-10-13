package com.johnwstump.incentivizer.model.pointstrategy;

import com.johnwstump.incentivizer.model.comparator.Comparator;
import com.johnwstump.incentivizer.model.pointstrategy.impl.IncrementalPointStrategy;
import com.johnwstump.incentivizer.model.pointstrategy.impl.ThresholdPointStrategy;

public abstract class PointStrategy {

    public abstract double determinePoints(double entryValue, double target, Comparator comparator);

    public static PointStrategy buildPointStrategy(String strategyCode) {
        switch (strategyCode) {
            case IncrementalPointStrategy.STRATEGY_CODE:
                return new IncrementalPointStrategy();
            case ThresholdPointStrategy.STRATEGY_CODE:
                return new ThresholdPointStrategy();
            default:
                throw new IllegalArgumentException("Unrecognized Point Strategy Code.");
        }
    }

    public abstract String getStrategyCode();
}
