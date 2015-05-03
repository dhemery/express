package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionsPolledBy;
import com.dhemery.express.helpers.PollingSchedules;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SubjectPredicatePolledSatisfactionTests {
    private static final String SUBJECT = "subject";

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock Poller poller;
    @Mock SelfDescribingPredicate<String> predicate;

    PollingSchedule schedule = PollingSchedules.random();
    PolledExpressions expressions;

    @Before
    public void setup() {
        expressions = new ExpressionsPolledBy(poller);
    }

    @Test
    public void returnsTrue_ifPollReturnsTrue() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, predicate);
            will(returnValue(true));
        }});

        boolean result = expressions.satisfiedThat(schedule, SUBJECT, predicate);

        assertThat(result, is(true));
    }

    @Test
    public void returnsFalse_ifPollReturnsFalse() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, predicate);
            will(returnValue(false));
        }});

        boolean result = expressions.satisfiedThat(schedule, SUBJECT, predicate);

        assertThat(result, is(false));
    }
}
