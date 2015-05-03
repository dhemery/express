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
import static org.hamcrest.Matchers.*;

// TODO: Package into nested classes
public class SubjectMatcherExpressionTests {
    private static final String SUBJECT = "subject";

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void assertThat_returnsWithoutThrowing_ifMatcherAcceptsSubject() {
        Expressions.assertThat(SUBJECT, anything());
    }

    @Test(expected = AssertionError.class)
    public void assertThat_throwsAssertionError_ifMatcherRejectsSubject() {
        Expressions.assertThat(SUBJECT, not(anything()));
    }

    @Test
    public void assertThat_errorMessageIncludesDiagnosis() {
        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, not(anything())));

        assertThat(message, is(Diagnosis.of(SUBJECT, not(anything()))));
    }

    @Test
    public void satisfiedThat_returnsTrue_ifMatcherAcceptsSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, anything());
        assertThat(result, is(true));
    }

    @Test
    public void satisfiedThat_returnsFalse_ifMatcherRejectsSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, not(anything()));
        assertThat(result, is(false));
    }
}
