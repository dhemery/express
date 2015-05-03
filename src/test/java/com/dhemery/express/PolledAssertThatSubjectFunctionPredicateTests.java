package com.dhemery.express;

import com.dhemery.express.helpers.PolledExpressionTestBase;
import com.dhemery.express.helpers.PollingSchedules;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Test;

import static com.dhemery.express.helpers.FunctionExpectations.applyReturns;
import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PolledAssertThatSubjectFunctionPredicateTests extends PolledExpressionTestBase {
    private static final String SUBJECT = "subject";
    private static final String FUNCTION_VALUE = "function value";
    private final PollingSchedule schedule = PollingSchedules.random();

    @Mock
    SelfDescribingFunction<String, String> function;
    @Mock
    SelfDescribingPredicate<String> predicate;

    @Before
    public void setup() {
        givenThat(applyReturns(function, SUBJECT, FUNCTION_VALUE));
    }

    @Test
    public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
        givenThat(pollReturns(schedule, SUBJECT, function, predicate, new PollEvaluationResult<>(FUNCTION_VALUE, true)));

        expressions.assertThat(schedule, SUBJECT, function, predicate);
    }

    @Test(expected = AssertionError.class)
    public void throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
        givenThat(pollReturns(schedule, SUBJECT, function, predicate, new PollEvaluationResult<>(FUNCTION_VALUE, false)));

        expressions.assertThat(schedule, SUBJECT, function, predicate);
    }

    @Test
    public void errorMessageIncludesDiagnosis() {
        givenThat(pollReturns(schedule, SUBJECT, function, predicate, new PollEvaluationResult<>(FUNCTION_VALUE, false)));


        String message = messageThrownBy(() -> expressions.assertThat(schedule, SUBJECT, function, predicate));

        assertThat(message, is(Diagnosis.of(schedule, SUBJECT, function, predicate, FUNCTION_VALUE)));
    }
}
