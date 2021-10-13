package com.johnwstump.incentivizer.services.metric;

import com.johnwstump.incentivizer.model.metric.Metric;

import java.util.List;
import java.util.Optional;

public interface IMetricService {
    Optional<Metric> getMetric(long metricId);

    List<Metric> getAllMetrics();

    List<Metric> getDefaultMetrics();
}
