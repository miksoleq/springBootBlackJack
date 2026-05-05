package com.codewithmiki.springbootblackjack.model;

import org.jspecify.annotations.NonNull;

public record Card(Value value, Colour colour) {

    @Override
    @NonNull
    public String toString() {
        return value + " of " + colour;
    }
}
