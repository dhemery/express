package com.dhemery.expressions.diagnosing;

import com.dhemery.expressions.SelfDescribingBooleanSupplier;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.util.function.BooleanSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class NamedBooleanSupplierTests {
    @Test
    public void returnsTrue_ifTheUnderlyingSupplierReturnsTrue() {
        BooleanSupplier underlyingSupplier = () -> true;
        BooleanSupplier supplier = new NamedBooleanSupplier("", underlyingSupplier);

        assertThat(supplier.getAsBoolean(), equalTo(underlyingSupplier.getAsBoolean()));
    }

    @Test
    public void returnsFalse_ifTheUnderlyingSupplierReturnsFalse() {
        BooleanSupplier underlyingSupplier = () -> false;
        BooleanSupplier supplier = new NamedBooleanSupplier("", underlyingSupplier);

        assertThat(supplier.getAsBoolean(), equalTo(underlyingSupplier.getAsBoolean()));
    }

    @Test
    public void describesItselfWithTheGivenName() {
        String name = "It was a dark and stormy night";
        SelfDescribingBooleanSupplier supplier = new NamedBooleanSupplier(name, () -> true);

        assertThat(StringDescription.toString(supplier), is(name));
        assertThat(String.valueOf(supplier), is(name));
    }
}
