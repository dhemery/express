package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionsPolledBy;
import com.dhemery.express.helpers.PollingSchedules;
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
import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SubjectFunctionMatcherPolledAssertionTests {
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
        context.checking(new Expectations() {{
            allowing(function).apply(SUBJECT);
            will(returnValue(FUNCTION_VALUE));
            allowing(any(SelfDescribing.class)).method("describeTo");
            will(appendItsStringValue());

            allowing(any(Matcher.class)).method("describeMismatch").with(any(String.class), any(Description.class));
            will(appendTheMismatchDescriptionOfTheItem());
        }});

        expressions = new ExpressionsPolledBy(poller);
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
