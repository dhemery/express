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
import static org.hamcrest.Matchers.sameInstance;

@RunWith(Enclosed.class)
public class SubjectFunctionPredicatePolledExpressionTests {

    public static final SelfDescribingFunction<String, String> FUNCTION = Named.function("function", String::toUpperCase);
    public static final SelfDescribingPredicate<String> PREDICATE = Named.predicate("predicate", t -> true);
    public static final String SUBJECT = "subject";

    public static class AssertThat extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            expressions.assertThat(schedule, SUBJECT, FUNCTION, PREDICATE);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            expressions.assertThat(schedule, SUBJECT, FUNCTION, PREDICATE);
        }

        @Test
        public void errorMessage_describesSubjectFunctionPredicateDerivedValueAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            String message = messageThrownBy(() -> expressions.assertThat(schedule, SUBJECT, FUNCTION, PREDICATE));

            assertThat(message, is(Diagnosis.of(schedule, SUBJECT, FUNCTION, PREDICATE, FUNCTION.apply(SUBJECT))));
        }
    }

    public static class SatisfiedThat extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsTrue_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            boolean result = expressions.satisfiedThat(schedule, SUBJECT, FUNCTION, PREDICATE);

            assertThat(result, is(true));
        }

        @Test
        public void returnsFalse_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            boolean result = expressions.satisfiedThat(schedule, SUBJECT, FUNCTION, PREDICATE);

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
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            expressions.waitUntil(SUBJECT, FUNCTION, PREDICATE);
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            expressions.waitUntil(SUBJECT, FUNCTION, PREDICATE);
        }

        @Test
        public void exceptionMessage_describesSubjectFunctionPredicateDerivedValueAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            String message = messageThrownBy(() -> expressions.waitUntil(SUBJECT, FUNCTION, PREDICATE));

            assertThat(message, is(Diagnosis.of(defaultSchedule, SUBJECT, FUNCTION, PREDICATE, FUNCTION.apply(SUBJECT))));
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
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            expressions.waitUntil(schedule, SUBJECT, FUNCTION, PREDICATE);
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            expressions.waitUntil(schedule, SUBJECT, FUNCTION, PREDICATE);
        }

        @Test
        public void exceptionMessage_describesSubjectFunctionPredicateDerivedValueAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            String message = messageThrownBy(() -> expressions.waitUntil(schedule, SUBJECT, FUNCTION, PREDICATE));

            assertThat(message, is(Diagnosis.of(schedule, SUBJECT, FUNCTION, PREDICATE, FUNCTION.apply(SUBJECT))));
        }
    }

    public static class WhenWithDefaultPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule defaultSchedule;

        @Before
        public void setUp() {
            defaultSchedule = expressions.eventually();
        }

        @Test
        public void returnsSubject_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            String returnedValue = expressions.when(SUBJECT, FUNCTION, PREDICATE);
            assertThat(returnedValue, is(sameInstance(SUBJECT)));
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            expressions.when(SUBJECT, FUNCTION, PREDICATE);
        }

        @Test
        public void exceptionMessage_describesSubjectFunctionPredicateDerivedValueAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            String message = messageThrownBy(() -> expressions.when(SUBJECT, FUNCTION, PREDICATE));

            assertThat(message, is(Diagnosis.of(defaultSchedule, SUBJECT, FUNCTION, PREDICATE, FUNCTION.apply(SUBJECT))));
        }
    }

    public static class WhenWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule schedule = PollingSchedules.random();

        @Before
        public void setUp() {
            schedule = expressions.eventually();
        }

        @Test
        public void returnsSubject_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            String returnedValue = expressions.when(schedule, SUBJECT, FUNCTION, PREDICATE);
            assertThat(returnedValue, is(sameInstance(SUBJECT)));
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            expressions.when(schedule, SUBJECT, FUNCTION, PREDICATE);
        }

        @Test
        public void exceptionMessage_describesSubjectFunctionPredicateDerivedValueAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            String message = messageThrownBy(() -> expressions.when(schedule, SUBJECT, FUNCTION, PREDICATE));

            assertThat(message, is(Diagnosis.of(schedule, SUBJECT, FUNCTION, PREDICATE, FUNCTION.apply(SUBJECT))));
        }
    }
}
