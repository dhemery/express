package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import com.dhemery.expressions.helpers.PolledExpressionTestSetup;
import com.dhemery.expressions.helpers.PollingSchedules;
import com.dhemery.expressions.polling.PollEvaluationResult;
import com.dhemery.expressions.polling.PollTimeoutException;
import org.jmock.Expectations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubjectFunctionPredicatePolledExpressionTests {
    private static final String SUBJECT = "subject";
    private static final Function<String, String> FUNCTION = Named.function("function", String::toUpperCase);
    private static final Predicate<String> PREDICATE = Named.predicate("predicate", t -> true);

    @Nested
    class AssertThat extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            expressions.assertThat(schedule, SUBJECT, FUNCTION, PREDICATE);
        }

        @Test
        void throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> expressions.assertThat(schedule, SUBJECT, FUNCTION, PREDICATE)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(schedule, SUBJECT, FUNCTION, PREDICATE, FUNCTION.apply(SUBJECT))));
        }

    }

    @Nested
    class SatisfiedThat extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        void returnsTrue_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            boolean result = expressions.satisfiedThat(schedule, SUBJECT, FUNCTION, PREDICATE);

            assertThat(result, is(true));
        }

        @Test
        void returnsFalse_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            boolean result = expressions.satisfiedThat(schedule, SUBJECT, FUNCTION, PREDICATE);

            assertThat(result, is(false));
        }
    }

    @Nested
    class WaitUntilWithDefaultPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule defaultSchedule;

        @BeforeEach
        void setUp() {
            defaultSchedule = expressions.eventually();
        }

        @Test
        void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            expressions.waitUntil(SUBJECT, FUNCTION, PREDICATE);
        }

        @Test
        void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.waitUntil(SUBJECT, FUNCTION, PREDICATE)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(defaultSchedule, SUBJECT, FUNCTION, PREDICATE, FUNCTION.apply(SUBJECT))));
        }
    }

    @Nested
    class WaitUntilWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule schedule = PollingSchedules.random();

        @BeforeEach
        void setUp() {
            schedule = expressions.eventually();
        }

        @Test
        void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            expressions.waitUntil(schedule, SUBJECT, FUNCTION, PREDICATE);
        }

        @Test
        void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.waitUntil(schedule, SUBJECT, FUNCTION, PREDICATE)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(schedule, SUBJECT, FUNCTION, PREDICATE, FUNCTION.apply(SUBJECT))));
        }
    }

    @Nested
    class WhenWithDefaultPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule defaultSchedule;

        @BeforeEach
        void setUp() {
            defaultSchedule = expressions.eventually();
        }

        @Test
        void returnsSubject_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            String returnedValue = expressions.when(SUBJECT, FUNCTION, PREDICATE);
            assertThat(returnedValue, is(sameInstance(SUBJECT)));
        }

        @Test
        void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.when(SUBJECT, FUNCTION, PREDICATE)
            );

            assertThat(thrown.getMessage(), is(Diagnosis.of(defaultSchedule, SUBJECT, FUNCTION, PREDICATE, FUNCTION.apply(SUBJECT))));
        }
    }

    @Nested
    class WhenWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule schedule = PollingSchedules.random();

        @BeforeEach
        void setUp() {
            schedule = expressions.eventually();
        }

        @Test
        void returnsSubject_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            String returnedValue = expressions.when(schedule, SUBJECT, FUNCTION, PREDICATE);
            assertThat(returnedValue, is(sameInstance(SUBJECT)));
        }

        @Test
        void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.when(schedule, SUBJECT, FUNCTION, PREDICATE)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(schedule, SUBJECT, FUNCTION, PREDICATE, FUNCTION.apply(SUBJECT))));
        }
    }
}
