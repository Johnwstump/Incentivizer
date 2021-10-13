package com.johnwstump.incentivizer.model.comparator;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public enum Comparator {
    LESS_THAN("<", (a, b) -> b - a, false, true),
    LESS_THAN_OR_EQUAL("<=", (a, b) -> b - a + 1, false, true),
    GREATER_THAN(">", (a, b) -> a - b, true, false),
    GREATER_THAN_OR_EQUAL(">=", (a, b) -> a - b + 1, true, false),
    EQUAL("=", (a, b) -> a.equals(b) ? a : 0, true, true);

    private String symbol = null;
    private final BiFunction<Double, Double, Double> computeFunction;

    private boolean forReward;
    private boolean canFail;

    Comparator(final String symbol,
               final BinaryOperator<Double> computeFunction,
               boolean forReward, boolean canFail) {
        this.symbol = symbol;
        this.computeFunction = computeFunction;
        this.forReward = forReward;
        this.canFail = canFail;
    }

    public boolean compare(double a, double b) {
        return computeFunction.apply(a, b) > 0;
    }

    public double getPoints(double a, double b) {
        return computeFunction.apply(a, b);
    }

    public String getSymbol() {
        return this.symbol;
    }

    public static Comparator getComparatorForSymbol(String symbol) {
        return Stream.of(Comparator.values())
                .filter(c -> c.getSymbol().equals(symbol))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean isForReward() {
        return this.forReward;
    }

    public boolean canFail() {
        return this.canFail;
    }
}
