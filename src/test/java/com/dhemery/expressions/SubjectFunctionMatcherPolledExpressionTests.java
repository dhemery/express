package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import com.dhemery.expressions.helpers.PolledExpressionTestSetup;
import com.dhemery.expressions.helpers.PollingSchedules;
import com.dhemery.expressions.polling.PollEvaluationResult;
import com.dhemery.expressions.polling.PollTimeoutException;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubjectFunctionMatcherPolledExpressionTests {
    private static final PollingSchedule SCHEDULE = PollingSchedules.random();
    private static final String SUBJECT = "subject";
    private static final Function<String, String> FUNCTION = Named.function("function", String::toUpperCase);
    private static final Matcher<Object> MATCHER = anything();

    @Nested
    class AssertThat extends PolledExpressionTestSetup {
        @Test
        void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            expressions.assertThat(SCHEDULE, SUBJECT, FUNCTION, MATCHER);
        }

        @Test
        void throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            AssertionError thrown = assertThrows(AssertionError.class,
                    () -> expressions.assertThat(SCHEDULE, SUBJECT, FUNCTION, MATCHER)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(SCHEDULE, SUBJECT, FUNCTION, MATCHER, FUNCTION.apply(SUBJECT))));
        }
    }

    @Nested
    class SatisfiedThat extends PolledExpressionTestSetup {
        @Test
        void returnsTrue_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            boolean result = expressions.satisfiedThat(SCHEDULE, SUBJECT, FUNCTION, MATCHER);

            assertThat(result, is(true));
        }

        @Test
        void returnsFalse_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            boolean result = expressions.satisfiedThat(SCHEDULE, SUBJECT, FUNCTION, MATCHER);

            assertThat(result, is(false));
        }
    }

    @Nested
    class WaitUntilWithDefaultPollingSchedule extends PolledExpressionTestSetup {
        PollingSchedule defaultSchedule;

        @BeforeEach
        void setup() {
            defaultSchedule = expressions.eventually();
        }

        @Test
        void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            expressions.waitUntil(SUBJECT, FUNCTION, MATCHER);
        }

        @Test
        void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.waitUntil(SUBJECT, FUNCTION, MATCHER)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(defaultSchedule, SUBJECT, FUNCTION, MATCHER, FUNCTION.apply(SUBJECT))));
        }
    }

    @Nested
    class WaitUntilWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            expressions.waitUntil(schedule, SUBJECT, FUNCTION, MATCHER);
        }

        @Test
        void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.waitUntil(schedule, SUBJECT, FUNCTION, MATCHER)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(schedule, SUBJECT, FUNCTION, MATCHER, FUNCTION.apply(SUBJECT))));
        }
    }

    @Nested
    class WhenWithDefaultPollingSchedule extends PolledExpressionTestSetup {
        static final String SUBJECT = SubjectFunctionMatcherPolledExpressionTests.SUBJECT;
        PollingSchedule defaultSchedule;

        @BeforeEach
        void setup() {
            defaultSchedule = expressions.eventually();
        }

        @Test
        void returnsSubject_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SubjectFunctionMatcherPolledExpressionTests.SUBJECT), true)));
            }});

            String returnedValue = expressions.when(SUBJECT, FUNCTION, MATCHER);
            assertThat(returnedValue, is(sameInstance(SUBJECT)));
        }

        @Test
        void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SubjectFunctionMatcherPolledExpressionTests.SUBJECT), false)));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.when(SUBJECT, FUNCTION, MATCHER)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(defaultSchedule, SubjectFunctionMatcherPolledExpressionTests.SUBJECT, FUNCTION, MATCHER, FUNCTION.apply(SubjectFunctionMatcherPolledExpressionTests.SUBJECT))));
        }
    }

    @Nested
    class WhenWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        void returnsSubject_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            String returnedValue = expressions.when(schedule, SUBJECT, FUNCTION, MATCHER);
            assertThat(returnedValue, is(sameInstance(SUBJECT)));
        }

        @Test
        void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.when(schedule, SUBJECT, FUNCTION, MATCHER)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(schedule, SUBJECT, FUNCTION, MATCHER, FUNCTION.apply(SUBJECT))));
        }
    }
}
