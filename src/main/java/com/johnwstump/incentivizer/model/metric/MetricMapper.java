package com.johnwstump.incentivizer.model.metric;

import com.johnwstump.incentivizer.model.metric.dto.MetricDisplay;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class MetricMapper {

    public MetricDisplay toDisplay(Metric metric) {
        MetricDisplay display = new MetricDisplay();
        display.setName(metric.getName());
        display.setId(metric.getId());
        return display;
    }
}
