package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionsPolledBy;
import com.dhemery.express.helpers.PollingSchedules;
import org.hamcrest.SelfDescribing;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.dhemery.express.helpers.Actions.appendItsStringValue;
import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SubjectPredicatePolledAssertionTests {
    private static final String subject = "subject";

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock Poller poller;
    @Mock SelfDescribingPredicate<String> predicate;

    PollingSchedule schedule = PollingSchedules.random();
    PolledExpressions expressions;

    @Before
    public void setup() {
        expressions = new ExpressionsPolledBy(poller);
        context.checking(new Expectations() {{
            allowing(any(SelfDescribing.class)).method("describeTo");
            will(appendItsStringValue());
        }});
    }

    @Test
    public void returnsWithoutThrowing_ifPollReturnsTrue() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, subject, predicate);
            will(returnValue(true));
        }});

        expressions.assertThat(schedule, subject, predicate);
    }

    @Test(expected = AssertionError.class)
    public void throwsAssertionError_ifPollReturnsFalse() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, subject, predicate);
            will(returnValue(false));
        }});

        expressions.assertThat(schedule, subject, predicate);
    }

    @Test
    public void errorMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, subject, predicate);
            will(returnValue(false));
        }});

        String message = messageThrownBy(() -> expressions.assertThat(schedule, subject, predicate));

        assertThat(message, is(Diagnosis.of(schedule, subject, predicate)));
    }

}
