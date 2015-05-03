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

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

// TODO: Package into nested classes
public class SubjectFunctionMatcherPolledExpressionTests {

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock Poller poller;

    PollingSchedule schedule = PollingSchedules.random();
    PolledExpressions expressions;
    SelfDescribingFunction<String, String> function = Named.function("function", String::toUpperCase);
    Matcher<Object> matcher = anything();

    @Before
    public void setup() {
        expressions = new ExpressionsPolledBy(poller);
    }

    @Test
    public void assertThat_returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, "subject", function, matcher);
            will(returnValue(new PollEvaluationResult<>(function.apply("subject"), true)));
        }});

        expressions.assertThat(schedule, "subject", function, matcher);
    }

    @Test(expected = AssertionError.class)
    public void assertThat_throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, "subject", function, matcher);
            will(returnValue(new PollEvaluationResult<>(function.apply("subject"), false)));
        }});

        expressions.assertThat(schedule, "subject", function, matcher);
    }

    @Test
    public void assertThat_errorMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, "subject", function, matcher);
            will(returnValue(new PollEvaluationResult<>(function.apply("subject"), false)));
        }});

        String message = messageThrownBy(() -> expressions.assertThat(schedule, "subject", function, matcher));

        assertThat(message, is(Diagnosis.of(schedule, "subject", function, matcher, function.apply("subject"))));
    }

    @Test
    public void satisfiedThat_returnsTrue_ifPollEvaluationResultIsSatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, "subject", function, matcher);
            will(returnValue(new PollEvaluationResult<>(function.apply("subject"), true)));
        }});

        boolean result = expressions.satisfiedThat(schedule, "subject", function, matcher);

        assertThat(result, is(true));
    }

    @Test
    public void satisfiedThat_returnsFalse_ifPollEvaluationResultIsDissatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, "subject", function, matcher);
            will(returnValue(new PollEvaluationResult<>(function.apply("subject"), false)));
        }});

        boolean result = expressions.satisfiedThat(schedule, "subject", function, matcher);

        assertThat(result, is(false));
    }
}
