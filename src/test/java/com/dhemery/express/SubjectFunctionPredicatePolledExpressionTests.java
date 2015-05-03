package com.dhemery.express;

import com.dhemery.express.helpers.PolledExpressionTestSetup;
import com.dhemery.express.helpers.PollingSchedules;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class SubjectFunctionPredicatePolledExpressionTests {

    public static final SelfDescribingFunction<String, String> FUNCTION = Named.function("function", String::toUpperCase);
    public static final SelfDescribingPredicate<String> PREDICATE = Named.predicate("predicate", t -> true);

    public static class AssertThat extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, "subject", FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), true)));
            }});

            expressions.assertThat(schedule, "subject", FUNCTION, PREDICATE);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, "subject", FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            expressions.assertThat(schedule, "subject", FUNCTION, PREDICATE);
        }

        @Test
        public void errorMessage_describesSubjectFunctionPredicateDerivedValueAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, "subject", FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            String message = messageThrownBy(() -> expressions.assertThat(schedule, "subject", FUNCTION, PREDICATE));

            assertThat(message, is(Diagnosis.of(schedule, "subject", FUNCTION, PREDICATE, FUNCTION.apply("subject"))));
        }
    }

    public static class SatisfiedThat extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsTrue_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, "subject", FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), true)));
            }});

            boolean result = expressions.satisfiedThat(schedule, "subject", FUNCTION, PREDICATE);

            assertThat(result, is(true));
        }

        @Test
        public void returnsFalse_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, "subject", FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            boolean result = expressions.satisfiedThat(schedule, "subject", FUNCTION, PREDICATE);

            assertThat(result, is(false));
        }
    }

    public static class WaitUntilWithDefaultPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule defaultSchedule;

        @Before
        public void setUp() {
            defaultSchedule = expressions.eventually();
        }

        @Test
        public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, "subject", FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), true)));
            }});

            expressions.waitUntil("subject", FUNCTION, PREDICATE);
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, "subject", FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            expressions.waitUntil("subject", FUNCTION, PREDICATE);
        }

        @Test
        public void exceptionMessage_describesSubjectFunctionPredicateDerivedValueAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, "subject", FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            String message = messageThrownBy(() -> expressions.waitUntil("subject", FUNCTION, PREDICATE));

            assertThat(message, is(Diagnosis.of(defaultSchedule, "subject", FUNCTION, PREDICATE, FUNCTION.apply("subject"))));
        }
    }

    public static class WaitUntilWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule schedule = PollingSchedules.random();

        @Before
        public void setUp() {
            schedule = expressions.eventually();
        }

        @Test
        public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, "subject", FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), true)));
            }});

            expressions.waitUntil(schedule, "subject", FUNCTION, PREDICATE);
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, "subject", FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            expressions.waitUntil(schedule, "subject", FUNCTION, PREDICATE);
        }

        @Test
        public void exceptionMessage_describesSubjectFunctionPredicateDerivedValueAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, "subject", FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            String message = messageThrownBy(() -> expressions.waitUntil(schedule, "subject", FUNCTION, PREDICATE));

            assertThat(message, is(Diagnosis.of(schedule, "subject", FUNCTION, PREDICATE, FUNCTION.apply("subject"))));
        }
    }
}
