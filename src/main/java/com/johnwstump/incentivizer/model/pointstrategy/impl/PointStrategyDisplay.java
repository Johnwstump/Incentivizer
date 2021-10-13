package com.johnwstump.incentivizer.model.pointstrategy.impl;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public @Data
class PointStrategyDisplay {
    private String strategyCode;
    private String displayName;
    private String description;

    enum PointStrategyDescription {

        INCREMENTAL(IncrementalPointStrategy.STRATEGY_CODE, "Incremental", "point(s) will be earned for every"),
        THRESHOLD(ThresholdPointStrategy.STRATEGY_CODE, "Threshold", "point(s) will be earned whenever # of");

        private String strategyCode;
        private String displayName;
        private String description;

        PointStrategyDescription(String strategyCode, String displayName, String description) {
            this.strategyCode = strategyCode;
            this.displayName = displayName;
            this.description = description;
        }
    }

    public PointStrategyDisplay(String strategyCode, String displayName, String description) {
        this.strategyCode = strategyCode;
        this.displayName = displayName;
        this.description = description;
    }

    public static PointStrategyDisplay getPointStrategyDisplay(String strategyCode) {
        for (PointStrategyDescription description : PointStrategyDescription.values()) {
            if (description.strategyCode.equals(strategyCode)) {
                return new PointStrategyDisplay(description.strategyCode, description.displayName, description.description);
            }
        }

        throw new IllegalArgumentException("No matching display object for strategy code " + strategyCode);
    }

    public static List<PointStrategyDisplay> getPointStrategyDisplays() {
        List<PointStrategyDisplay> strategies = new ArrayList<>();

        for (PointStrategyDescription description : PointStrategyDescription.values()) {
            PointStrategyDisplay display = new PointStrategyDisplay(description.strategyCode, description.displayName, description.description);
            strategies.add(display);
        }

        return strategies;
    }
}
