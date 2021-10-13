package com.johnwstump.incentivizer.model.metric.dto;

import com.johnwstump.incentivizer.model.metric.Metric;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;

@NoArgsConstructor
public @Data
class MetricDisplay {

    private long id;
    private String name;

    public MetricDisplay(Metric metric) {
        this.name = metric.getName();
        this.id = metric.getId();
    }

    /**
     * @return the Name of this metric formatted in title case.
     */
    public String getViewName() {
        return WordUtils.capitalizeFully(getName());
    }
}
