package com.dhemery.express;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.dhemery.express.helpers.Actions.appendItsStringValue;
import static com.dhemery.express.helpers.Actions.appendTheMismatchDescriptionOfTheItem;
import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BooleanSupplierAssertionTests {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock
    SelfDescribingBooleanSupplier supplier;

    @Before
    public void setup() {
        context.checking(new Expectations() {{
            allowing(any(SelfDescribing.class)).method("describeTo");
            will(appendItsStringValue());
        }});
    }

    @Test
    public void returnsWithoutThrowing_ifSupplierReturnsTrue() {
        context.checking(new Expectations() {{
            allowing(supplier).getAsBoolean();
            will(returnValue(true));
        }});

        Expressions.assertThat(supplier);
    }

    @Test(expected = AssertionError.class)
    public void throwsAssertionError_ifSupplierReturnsFalse() {
        context.checking(new Expectations() {{
            allowing(supplier).getAsBoolean();
            will(returnValue(false));
        }});

        Expressions.assertThat(supplier);
    }

    @Test
    public void errorMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{
            allowing(supplier).getAsBoolean();
            will(returnValue(false));
        }});

        String message = messageThrownBy(() -> Expressions.assertThat(supplier));

        assertThat(message, is(Diagnosis.of(supplier)));
    }
}
