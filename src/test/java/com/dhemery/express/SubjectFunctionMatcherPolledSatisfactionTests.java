package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionsPolledBy;
import com.dhemery.express.helpers.PollingSchedules;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SubjectFunctionMatcherPolledSatisfactionTests {
    private static final String SUBJECT = "subject";
    private static final String FUNCTION_VALUE = "function value";

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock Poller poller;
    @Mock SelfDescribingFunction<String, String> function;
    @Mock Matcher<String> matcher;


    PollingSchedule schedule = PollingSchedules.random();
    PolledExpressions expressions;

    @Before
    public void setup() {
        context.checking(new Expectations(){{
            allowing(function).apply(SUBJECT);
            will(returnValue(FUNCTION_VALUE));
        }});

        expressions = new ExpressionsPolledBy(poller);
    }

    @Test
    public void returnsTrue_ifPollEvaluationResultIsSatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, function, matcher);
            will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, true)));
        }});

        boolean result = expressions.satisfiedThat(schedule, SUBJECT, function, matcher);

        assertThat(result, is(true));
    }

    @Test
    public void returnsFalse_ifPollEvaluationResultIsDissatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, function, matcher);
            will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, false)));
        }});

        boolean result = expressions.satisfiedThat(schedule, SUBJECT, function, matcher);

        assertThat(result, is(false));
    }
}
