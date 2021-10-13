package com.johnwstump.incentivizer.model.pointstrategy.impl;

import com.johnwstump.incentivizer.model.pointstrategy.PointStrategy;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PointStrategyConverter implements AttributeConverter<PointStrategy, String> {
    @Override
    public String convertToDatabaseColumn(PointStrategy pointStrategy) {
        return pointStrategy.getStrategyCode();
    }

    @Override
    public PointStrategy convertToEntityAttribute(String symbol) {
        return PointStrategy.buildPointStrategy(symbol);
    }
}
