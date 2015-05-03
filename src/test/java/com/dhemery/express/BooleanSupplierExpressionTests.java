package com.dhemery.express;

import org.junit.Test;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BooleanSupplierExpressionTests {
    @Test
    public void assertThat_returnsWithoutThrowing_ifSupplierReturnsTrue() {
        Expressions.assertThat(supplierOf(true));
    }

    @Test(expected = AssertionError.class)
    public void assertThat_throwsAssertionError_ifSupplierReturnsFalse() {
        Expressions.assertThat(supplierOf(false));
    }

    @Test
    public void assertThat_errorMessageIncludesDiagnosis() {
        String message = messageThrownBy(() -> Expressions.assertThat(supplierOf(false)));

        assertThat(message, is(Diagnosis.of(supplierOf(false))));
    }

    @Test
    public void satisfiedThat_returnsTrue_ifSupplierReturnsTrue() {

        assertThat(Expressions.satisfiedThat(supplierOf(true)), is(true));
    }

    @Test
    public void satisfiedThat_returnsFalse_ifSupplierReturnsFalse() {

        assertThat(Expressions.satisfiedThat(supplierOf(false)), is(false));
    }

    private SelfDescribingBooleanSupplier supplierOf(boolean supplied) {
        return Named.booleanSupplier(String.valueOf(supplied), () -> supplied);
    }
}
