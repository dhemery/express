package com.dhemery.express;

import org.junit.Test;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

// TODO: Package into nested classes
public class BooleanSupplierExpressionTests {
    @Test
    public void assertThat_returnsWithoutThrowing_ifSupplierReturnsTrue() {
        SelfDescribingBooleanSupplier supplier = Named.booleanSupplier("", () -> true);

        Expressions.assertThat(supplier);
    }

    @Test(expected = AssertionError.class)
    public void assertThat_throwsAssertionError_ifSupplierReturnsFalse() {
        SelfDescribingBooleanSupplier supplier = Named.booleanSupplier("", () -> false);

        Expressions.assertThat(supplier);
    }

    @Test
    public void assertThat_errorMessageIncludesDiagnosis() {
        SelfDescribingBooleanSupplier supplier = Named.booleanSupplier("supplier", () -> false);

        String message = messageThrownBy(() -> Expressions.assertThat(supplier));

        assertThat(message, is(Diagnosis.of(supplier)));
    }

    @Test
    public void satisfiedThat_returnsTrue_ifSupplierReturnsTrue() {
        assertThat(Expressions.satisfiedThat(() -> true), is(true));
    }

    @Test
    public void satisfiedThat_returnsFalse_ifSupplierReturnsFalse() {
        assertThat(Expressions.satisfiedThat(() -> false), is(false));
    }

}
