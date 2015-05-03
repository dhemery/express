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

public class SubjectPredicateAssertionTests {
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
    public void returnsWithoutThrowing_ifMatcherAcceptsSubject() {
        context.checking(new Expectations() {{
            allowing(predicate).test(SUBJECT);
            will(returnValue(true));
        }});

        Expressions.assertThat(SUBJECT, predicate);
    }

    @Test(expected = AssertionError.class)
    public void throwsAssertionError_ifMatcherRejectsSubject() {
        context.checking(new Expectations() {{
            allowing(predicate).test(SUBJECT);
            will(returnValue(false));
        }});

        Expressions.assertThat(SUBJECT, predicate);
    }

    @Test
    public void diagnosisDescribes_matcher_mismatchOfSubject() {
        context.checking(new Expectations() {{
            allowing(predicate).test(SUBJECT);
            will(returnValue(false));
        }});

        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, predicate));

        assertThat(message, is(Diagnosis.of(SUBJECT, predicate)));
    }
}
