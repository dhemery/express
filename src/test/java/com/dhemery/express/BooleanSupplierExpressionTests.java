package com.dhemery.express;

import org.hamcrest.SelfDescribing;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.dhemery.express.helpers.Actions.appendItsStringValue;
import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BooleanSupplierExpressionTests {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock SelfDescribingBooleanSupplier supplier;

    @Before
    public void setup() {
        context.checking(new Expectations() {{
            allowing(any(SelfDescribing.class)).method("describeTo");
            will(appendItsStringValue());
        }});
    }

    @Test
    public void assertThat_returnsWithoutThrowing_ifSupplierReturnsTrue() {
        givenThat(supplierReturns(true));

        Expressions.assertThat(supplier);
    }

    @Test(expected = AssertionError.class)
    public void assertThat_throwsAssertionError_ifSupplierReturnsFalse() {
        givenThat(supplierReturns(false));

        Expressions.assertThat(supplier);
    }

    @Test
    public void assertThat_errorMessageIncludesDiagnosis() {
        givenThat(supplierReturns(false));

        String message = messageThrownBy(() -> Expressions.assertThat(supplier));

        assertThat(message, is(Diagnosis.of(supplier)));
    }

    @Test
    public void satisfiedThat_returnsTrue_ifSupplierReturnsTrue() {
        givenThat(supplierReturns(true));

        boolean result = Expressions.satisfiedThat(supplier);

        assertThat(result, is(true));
    }

    @Test
    public void satisfiedThat_returnsFalse_ifSupplierReturnsFalse() {
        givenThat(supplierReturns(false));

        boolean result = Expressions.satisfiedThat(supplier);

        assertThat(result, is(false));
    }

    private void givenThat(Expectations expectations) {
        context.checking(expectations);
    }

    private Expectations supplierReturns(final boolean returnValue) {
        return new Expectations() {{
            allowing(supplier).getAsBoolean();
            will(returnValue(returnValue));
        }};
    }
}
