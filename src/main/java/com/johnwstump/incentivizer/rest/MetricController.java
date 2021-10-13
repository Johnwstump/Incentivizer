package com.johnwstump.incentivizer.rest;

import com.johnwstump.incentivizer.model.metric.Metric;
import com.johnwstump.incentivizer.model.metric.dto.MetricDisplay;
import com.johnwstump.incentivizer.services.metric.IMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/metric")
public class MetricController extends AbstractRestController {
    private IMetricService metricService;

    @Autowired
    public MetricController(IMetricService metricService) {
        this.metricService = metricService;
    }

    @GetMapping("/")
    public ResponseEntity<List<MetricDisplay>> getMetrics() {

        List<MetricDisplay> metricDTOs = new ArrayList<>();

        for (Metric metric : metricService.getAllMetrics()) {
            metricDTOs.add(new MetricDisplay(metric));
        }

        return new ResponseEntity<>(metricDTOs, HttpStatus.OK);
    }
}
