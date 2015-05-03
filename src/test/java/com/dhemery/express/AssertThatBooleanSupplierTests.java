package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionTestBase;
import com.dhemery.express.helpers.FunctionExpectations;
import org.jmock.auto.Mock;
import org.junit.Test;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AssertThatBooleanSupplierTests extends ExpressionTestBase {

    @Mock
    SelfDescribingBooleanSupplier supplier;

    @Test
    public void returnsWithoutThrowing_ifSupplierReturnsTrue() {
        givenThat(FunctionExpectations.getAsBooleanReturns(supplier, true));

        Expressions.assertThat(supplier);
    }

    @Test(expected = AssertionError.class)
    public void throwsAssertionError_ifSupplierReturnsFalse() {
        givenThat(FunctionExpectations.getAsBooleanReturns(supplier, false));

        Expressions.assertThat(supplier);
    }

    @Test
    public void errorMessageIncludesDiagnosis() {
        givenThat(FunctionExpectations.getAsBooleanReturns(supplier, false));

        String message = messageThrownBy(() -> Expressions.assertThat(supplier));

        assertThat(message, is(Diagnosis.of(supplier)));
    }
}
