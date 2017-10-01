package com.dhemery.expressions.diagnosing;

import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class NamedBooleanSupplierTests {
    @Test
    void returnsTrue_ifTheUnderlyingSupplierReturnsTrue() {
        BooleanSupplier underlyingSupplier = () -> true;
        BooleanSupplier supplier = new NamedBooleanSupplier("", underlyingSupplier);

        assertThat(supplier.getAsBoolean(), equalTo(underlyingSupplier.getAsBoolean()));
    }

    @Test
    void returnsFalse_ifTheUnderlyingSupplierReturnsFalse() {
        BooleanSupplier underlyingSupplier = () -> false;
        BooleanSupplier supplier = new NamedBooleanSupplier("", underlyingSupplier);

        assertThat(supplier.getAsBoolean(), equalTo(underlyingSupplier.getAsBoolean()));
    }

    @Test
    void describesItselfWithTheGivenName() {
        String name = "It was a dark and stormy night";
        BooleanSupplier supplier = new NamedBooleanSupplier(name, () -> true);

        assertThat(String.valueOf(supplier), is(name));
    }
}
