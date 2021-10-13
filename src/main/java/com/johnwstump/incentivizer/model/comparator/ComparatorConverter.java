package com.johnwstump.incentivizer.model.comparator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ComparatorConverter implements AttributeConverter<Comparator, String> {
    @Override
    public String convertToDatabaseColumn(Comparator comparator) {
        return comparator.getSymbol();
    }

    @Override
    public Comparator convertToEntityAttribute(String symbol) {
        if (symbol == null) {
            return null;
        }

        return Comparator.getComparatorForSymbol(symbol);
    }
}
