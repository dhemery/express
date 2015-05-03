package com.dhemery.express.helpers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;

import static com.dhemery.express.helpers.FunctionExpectations.appendItsStringValue;
import static com.dhemery.express.helpers.FunctionExpectations.appendTheMismatchDescriptionOfTheItem;

public class ExpressionTestBase {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Before
    public void expressionTestBaseSetup() {
        context.checking(new Expectations() {{ //@formatter:off
            allowing(any(SelfDescribing.class)).method("describeTo");
                will(appendItsStringValue());
            allowing(any(Matcher.class)).method("describeMismatch").with(any(String.class), any(Description.class));
                will(appendTheMismatchDescriptionOfTheItem());
        }}); //@formatter:on
    }

    public void givenThat(Expectations expectation) {
        context.checking(expectation);
    }
}
