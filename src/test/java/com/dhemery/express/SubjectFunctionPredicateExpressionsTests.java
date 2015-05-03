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
import static org.hamcrest.Matchers.is;

public class SubjectFunctionPredicateExpressionsTests {
    private static final String SUBJECT = "subject";
    private static final String FUNCTION_VALUE = "function value";

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock SelfDescribingFunction<String, String> function;
    @Mock SelfDescribingPredicate<String> predicate;

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
    public void assertThat_returnsWithoutThrowing_ifPredicateAcceptsFunctionOfSubject() {
        context.checking(new Expectations() {{
            allowing(predicate).test(FUNCTION_VALUE);
            will(returnValue(true));
        }});

        Expressions.assertThat(SUBJECT, function, predicate);
    }

    @Test(expected = AssertionError.class)
    public void assertThat_throwsAssertionError_ifPredicateRejectsFunctionOfSubject() {
        context.checking(new Expectations() {{
            allowing(predicate).test(FUNCTION_VALUE);
            will(returnValue(false));
        }});

        Expressions.assertThat(SUBJECT, function, predicate);
    }

    @Test
    public void assertThat_errorMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{
            allowing(predicate).test(FUNCTION_VALUE);
            will(returnValue(false));
        }});

        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, function, predicate));

        assertThat(message, is(Diagnosis.of(SUBJECT, function, predicate, FUNCTION_VALUE)));
    }

    @Test
    public void satisfiedThat_returnsTrue_ifPredicateAcceptsFunctionOfSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, s -> FUNCTION_VALUE, s -> true);
        assertThat(result, is(true));
    }

    @Test
    public void satisfiedThat_returnsFalse_ifPredicateRejectsFunctionOfSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, s -> FUNCTION_VALUE, s -> false);
        assertThat(result, is(false));
    }
}
