package com.codewithmiki.springbootblackjack.model;

public enum Value {
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10),
    ACE(11);

    private final int numericValue;

    Value(int numericValue) {
        this.numericValue = numericValue;
    }
    public int getNumericValue() {
        return numericValue;
    }

}
