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

import static com.dhemery.express.helpers.Actions.selfDescribersDescribeThemselves;
import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SubjectFunctionMatcherPolledAssertionTests {
    private static final String SUBJECT = "subject";
    private static final String FUNCTION_VALUE = "function value";
    private final PollingSchedule schedule = PollingSchedules.random();
    @Mock
    public Poller poller;
    public PolledExpressions expressions;
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    SelfDescribingFunction<String, String> function;
    @Mock
    Matcher<String> matcher;

    @Before
    public void setup() {
        context.checking(new Expectations() {{
            allowing(function).apply(SUBJECT);
            will(returnValue(FUNCTION_VALUE));
        }});

        expressions = new ExpressionsPolledBy(poller);
        context.checking(selfDescribersDescribeThemselves());
    }

    @Test
    public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, function, matcher);
            will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, true)));
        }});

        expressions.assertThat(schedule, SUBJECT, function, matcher);
    }

    @Test(expected = AssertionError.class)
    public void throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, function, matcher);
            will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, false)));
        }});

        expressions.assertThat(schedule, SUBJECT, function, matcher);
    }

    @Test
    public void errorMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, function, matcher);
            will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, false)));
        }});

        String message = messageThrownBy(() -> expressions.assertThat(schedule, SUBJECT, function, matcher));

        assertThat(message, is(Diagnosis.of(schedule, SUBJECT, function, matcher, FUNCTION_VALUE)));
    }
}
