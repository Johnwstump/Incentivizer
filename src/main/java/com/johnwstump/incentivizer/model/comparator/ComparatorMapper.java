package com.johnwstump.incentivizer.model.comparator;

import com.johnwstump.incentivizer.model.comparator.dto.ComparatorDisplay;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ComparatorMapper {

    public ComparatorDisplay toDisplay(Comparator comparator) {
        ComparatorDisplay display = new ComparatorDisplay();
        display.setSymbol(comparator.getSymbol());
        display.setForReward(comparator.isForReward());
        return display;
    }
}
