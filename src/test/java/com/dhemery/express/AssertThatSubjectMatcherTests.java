package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionTestBase;
import com.dhemery.express.helpers.FunctionExpectations;
import com.dhemery.express.helpers.Throwables;
import org.hamcrest.Matcher;
import org.jmock.auto.Mock;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AssertThatSubjectMatcherTests extends ExpressionTestBase {
    private static final String SUBJECT = "subject";

    @Mock
    Matcher<String> matcher;

    @Test
    public void returnsWithoutThrowing_ifMatcherAcceptsSubject() {
        givenThat(FunctionExpectations.matchesReturns(matcher, SUBJECT, true));

        Expressions.assertThat(SUBJECT, matcher);
    }

    @Test(expected = AssertionError.class)
    public void throwsAssertionError_ifMatcherRejectsSubject() {
        givenThat(FunctionExpectations.matchesReturns(matcher, SUBJECT, false));

        Expressions.assertThat(SUBJECT, matcher);
    }

    @Test
    public void diagnosisDescribes_matcher_mismatchOfSubject() {
        givenThat(FunctionExpectations.matchesReturns(matcher, SUBJECT, false));

        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, matcher));

        assertThat(message, is(Diagnosis.of(SUBJECT, matcher)));
    }
}
