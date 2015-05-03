package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionTestBase;
import com.dhemery.express.helpers.FunctionExpectations;
import com.dhemery.express.helpers.Throwables;
import org.hamcrest.Matcher;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AssertThatSubjectFunctionMatcherTests extends ExpressionTestBase {
    private static final String SUBJECT = "subject";
    private static final String FUNCTION_VALUE = "function value";

    @Mock
    SelfDescribingFunction<String, String> function;
    @Mock
    Matcher<String> matcher;

    @Before
    public void setup() {
        givenThat(FunctionExpectations.applyReturns(function, SUBJECT, FUNCTION_VALUE));
    }

    @Test
    public void returnsWithoutThrowing_ifMatcherAcceptsFunctionOfSubject() {
        givenThat(FunctionExpectations.matchesReturns(matcher, FUNCTION_VALUE, true));

        Expressions.assertThat(SUBJECT, function, matcher);
    }

    @Test(expected = AssertionError.class)
    public void throwsAssertionError_ifMatcherRejectsFunctionOfSubject() {
        givenThat(FunctionExpectations.matchesReturns(matcher, FUNCTION_VALUE, false));

        Expressions.assertThat(SUBJECT, function, matcher);
    }

    @Test
    public void messageIncludesDiagnosis() {
        givenThat(FunctionExpectations.matchesReturns(matcher, FUNCTION_VALUE, false));

        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, function, matcher));

        assertThat(message, is(Diagnosis.of(SUBJECT, function, matcher, FUNCTION_VALUE)));
    }
}
