package com.johnwstump.incentivizer.model.pointstrategy;

import com.johnwstump.incentivizer.model.pointstrategy.impl.PointStrategyDisplay;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class PointStrategyMapper {
    public PointStrategyDisplay toDisplay(PointStrategy strategy) {
        return PointStrategyDisplay.getPointStrategyDisplay(strategy.getStrategyCode());
    }
}
