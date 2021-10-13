package com.johnwstump.incentivizer.dao;

import com.johnwstump.incentivizer.model.metric.Metric;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository extends CrudRepository<Metric, Long> {
}
