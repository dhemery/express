package com.dhemery.express;

import com.dhemery.express.helpers.Throwables;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class SubjectFunctionMatcherAssertionTests {
    private static final String SUBJECT = "subject";

    private static final String FUNCTION_VALUE = "function value";
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock
    SelfDescribingFunction<String, String> function;

    @Mock
    Matcher<String> matcher;

    @Before
    public void setup() {
        context.checking(new Expectations() {{
            allowing(function).apply(SUBJECT);
            will(returnValue(FUNCTION_VALUE));

            allowing(any(SelfDescribing.class)).method("describeTo");
            will(appendItsStringValue());

            allowing(any(Matcher.class)).method("describeMismatch").with(any(String.class), any(Description.class));
            will(appendTheMismatchDescriptionOfTheItem());
        }});
    }

    @Test
    public void returnsWithoutThrowing_ifMatcherAcceptsFunctionOfSubject() {
        context.checking(new Expectations() {{
            allowing(same(matcher)).method("matches").with(equalTo(FUNCTION_VALUE));
            will(returnValue(true));
        }});

        Expressions.assertThat(SUBJECT, function, matcher);
    }

    @Test(expected = AssertionError.class)
    public void throwsAssertionError_ifMatcherRejectsFunctionOfSubject() {
        context.checking(new Expectations() {{
            allowing(same(matcher)).method("matches").with(equalTo(FUNCTION_VALUE));
            will(returnValue(false));
        }});

        Expressions.assertThat(SUBJECT, function, matcher);
    }

    @Test
    public void messageIncludesDiagnosis() {
        context.checking(new Expectations() {{
            allowing(same(matcher)).method("matches").with(equalTo(FUNCTION_VALUE));
            will(returnValue(false));
        }});

        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, function, matcher));

        assertThat(message, is(Diagnosis.of(SUBJECT, function, matcher, FUNCTION_VALUE)));
    }
}
