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

public class SubjectFunctionMatcherExpressionTests {
    private static final String SUBJECT = "subject";
    private static final String FUNCTION_VALUE = "function value";

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock SelfDescribingFunction<String, String> function;
    @Mock Matcher<String> matcher;

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
    public void assertThat_returnsWithoutThrowing_ifMatcherAcceptsFunctionOfSubject() {
        givenThat(matcherAccepts(FUNCTION_VALUE));

        Expressions.assertThat(SUBJECT, function, matcher);
    }

    @Test(expected = AssertionError.class)
    public void assertThat_throwsAssertionError_ifMatcherRejectsFunctionOfSubject() {
        givenThat(matcherRejects(FUNCTION_VALUE));

        Expressions.assertThat(SUBJECT, function, matcher);
    }

    @Test
    public void assertThat_errorMessageIncludesDiagnosis() {
        givenThat(matcherRejects(FUNCTION_VALUE));

        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, function, matcher));

        assertThat(message, is(Diagnosis.of(SUBJECT, function, matcher, FUNCTION_VALUE)));
    }

    @Test
    public void satisfiedThat_returnsTrue_ifMatcherAcceptsFunctionOfSubject() {
        givenThat(matcherAccepts(FUNCTION_VALUE));

        boolean result = Expressions.satisfiedThat(SUBJECT, function, matcher);

        assertThat(result, is(true));
    }

    @Test
    public void satisfiedThat_returnsFalse_ifMatcherRejectsFunctionOfSubject() {
        givenThat(matcherRejects(FUNCTION_VALUE));

        boolean result = Expressions.satisfiedThat(SUBJECT, function, matcher);

        assertThat(result, is(false));
    }

    private Expectations matcherAccepts(final String input) {
        return new Expectations() {{
            allowing(same(matcher)).method("matches").with(equalTo(input));
            will(returnValue(true));
        }};
    }

    private Expectations matcherRejects(final String input) {
        return new Expectations() {{
            allowing(same(matcher)).method("matches").with(equalTo(input));
            will(returnValue(false));
        }};
    }

    private void givenThat(Expectations expectations) {
        context.checking(expectations);
    }
}
