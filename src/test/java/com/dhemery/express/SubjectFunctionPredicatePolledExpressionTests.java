package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionsPolledBy;
import com.dhemery.express.helpers.PollingSchedules;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SubjectFunctionPredicatePolledExpressionTests {
    private static final String SUBJECT = "subject";

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock Poller poller;

    SelfDescribingFunction<String, String> function = Named.function("some function", String::toUpperCase);
    SelfDescribingPredicate<String> predicate = Named.predicate("some predicate", t -> true);
    PollingSchedule schedule = PollingSchedules.random();
    PolledExpressions expressions;

    @Before
    public void setup() {
        expressions = new ExpressionsPolledBy(poller);
    }

    @Test
    public void asserThat_returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, function, predicate);
            will(returnValue(new PollEvaluationResult<>(function.apply(SUBJECT), true)));
        }});

        expressions.assertThat(schedule, SUBJECT, function, predicate);
    }

    @Test(expected = AssertionError.class)
    public void asserThat_throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, function, predicate);
            will(returnValue(new PollEvaluationResult<>(function.apply(SUBJECT), false)));
        }});

        expressions.assertThat(schedule, SUBJECT, function, predicate);
    }

    @Test
    public void asserThat_errorMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, function, predicate);
            will(returnValue(new PollEvaluationResult<>(function.apply(SUBJECT), false)));
        }});

        String message = messageThrownBy(() -> expressions.assertThat(schedule, SUBJECT, function, predicate));

        assertThat(message, is(Diagnosis.of(schedule, SUBJECT, function, predicate, function.apply(SUBJECT))));
    }

    @Test
    public void satisfiedThat_returnsTrue_ifPollEvaluationResultIsSatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, function, predicate);
            will(returnValue(new PollEvaluationResult<>(function.apply(SUBJECT), true)));
        }});

        boolean result = expressions.satisfiedThat(schedule, SUBJECT, function, predicate);

        assertThat(result, is(true));
    }

    @Test
    public void satisfiedThat_returnsFalse_ifPollEvaluationResultIsDissatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, function, predicate);
            will(returnValue(new PollEvaluationResult<>(function.apply(SUBJECT), false)));
        }});

        boolean result = expressions.satisfiedThat(schedule, SUBJECT, function, predicate);

        assertThat(result, is(false));
    }
}
