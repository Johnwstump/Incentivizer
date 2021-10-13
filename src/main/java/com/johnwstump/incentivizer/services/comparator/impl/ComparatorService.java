package com.johnwstump.incentivizer.services.comparator.impl;

import com.johnwstump.incentivizer.model.comparator.Comparator;
import com.johnwstump.incentivizer.services.comparator.IComparatorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ComparatorService implements IComparatorService {

    @Override
    public List<Comparator> getAllComparators() {
        return new ArrayList<>(Arrays.asList(Comparator.values()));
    }
}
