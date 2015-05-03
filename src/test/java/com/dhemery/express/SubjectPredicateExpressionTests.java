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

public class SubjectPredicateExpressionTests {
    private static final String SUBJECT = "subject";

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock SelfDescribingPredicate<String> predicate;

    @Before
    public void setup() {
        context.checking(new Expectations() {{
            allowing(any(SelfDescribing.class)).method("describeTo");
            will(appendItsStringValue());

            allowing(any(Matcher.class)).method("describeMismatch").with(any(String.class), any(Description.class));
            will(appendTheMismatchDescriptionOfTheItem());
        }});
    }

    @Test
    public void assertThat_returnsWithoutThrowing_ifMatcherAcceptsSubject() {
        context.checking(new Expectations() {{
            allowing(predicate).test(SUBJECT);
            will(returnValue(true));
        }});

        Expressions.assertThat(SUBJECT, predicate);
    }

    @Test(expected = AssertionError.class)
    public void assertThat_throwsAssertionError_ifMatcherRejectsSubject() {
        context.checking(new Expectations() {{
            allowing(predicate).test(SUBJECT);
            will(returnValue(false));
        }});

        Expressions.assertThat(SUBJECT, predicate);
    }

    @Test
    public void assertThat_errorMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{
            allowing(predicate).test(SUBJECT);
            will(returnValue(false));
        }});

        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, predicate));

        assertThat(message, is(Diagnosis.of(SUBJECT, predicate)));
    }

    @Test
    public void satisfiedThat_returnsTrue_ifPredicateAcceptsSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, s -> true);
        assertThat(result, is(true));
    }

    @Test
    public void satisfiedThat_returnsFalse_ifPredicateRejectsSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, s -> false);
        assertThat(result, is(false));
    }
}
