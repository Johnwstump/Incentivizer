package com.johnwstump.incentivizer.services.metric.impl;

import com.johnwstump.incentivizer.dao.MetricRepository;
import com.johnwstump.incentivizer.model.metric.Metric;
import com.johnwstump.incentivizer.services.metric.IMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class MetricService implements IMetricService {
    private final MetricRepository metricRepository;

    @Autowired
    public MetricService(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Override
    public Optional<Metric> getMetric(long metricId) {
        return metricRepository.findById(metricId);
    }

    @Override
    public List<Metric> getAllMetrics() {
        List<Metric> metrics = new ArrayList<>();
        metricRepository.findAll().forEach(metrics::add);
        return metrics;
    }

    @Override
    public List<Metric> getDefaultMetrics() {
        return getAllMetrics()
                .stream()
                .filter(Metric::isDefault)
                .collect(Collectors.toList());
    }
}
