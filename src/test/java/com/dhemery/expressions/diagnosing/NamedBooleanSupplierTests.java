package com.dhemery.expressions.diagnosing;

import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NamedBooleanSupplierTests {
    @Test
    void returnsTrue_ifTheUnderlyingSupplierReturnsTrue() {
        BooleanSupplier underlyingSupplier = () -> true;
        BooleanSupplier supplier = new NamedBooleanSupplier("", underlyingSupplier);

        assertEquals(underlyingSupplier.getAsBoolean(), supplier.getAsBoolean());
    }

    @Test
    void returnsFalse_ifTheUnderlyingSupplierReturnsFalse() {
        BooleanSupplier underlyingSupplier = () -> false;
        BooleanSupplier supplier = new NamedBooleanSupplier("", underlyingSupplier);

        assertEquals(underlyingSupplier.getAsBoolean(), supplier.getAsBoolean());
    }

    @Test
    void describesItselfWithTheGivenName() {
        String name = "It was a dark and stormy night";
        BooleanSupplier supplier = new NamedBooleanSupplier(name, () -> true);

        assertEquals(name, String.valueOf(supplier));
    }
}
