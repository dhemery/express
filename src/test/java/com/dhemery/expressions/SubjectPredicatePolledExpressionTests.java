package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import com.dhemery.expressions.helpers.PolledExpressionTestSetup;
import com.dhemery.expressions.helpers.PollingSchedules;
import com.dhemery.expressions.polling.PollTimeoutException;
import org.jmock.Expectations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubjectPredicatePolledExpressionTests {
    private static final String SUBJECT = "subject";
    private static final Predicate<String> PREDICATE = Named.predicate("predicate", t -> true);

    @Nested
    class AssertThat extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(true));
            }});

            expressions.assertThat(schedule, SUBJECT, PREDICATE);
        }

        @Test
        void throwsAssertionError_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> expressions.assertThat(schedule, SUBJECT, PREDICATE)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(schedule, SUBJECT, PREDICATE)));
        }
    }

    static class SatisfiedThat extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        void returnsTrue_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(true));
            }});

            boolean result = expressions.satisfiedThat(schedule, SUBJECT, PREDICATE);

            assertThat(result, is(true));
        }

        @Test
        void returnsFalse_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            boolean result = expressions.satisfiedThat(schedule, SUBJECT, PREDICATE);

            assertThat(result, is(false));
        }
    }

    static class WaitUntilWithDefaultPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule defaultSchedule;

        @BeforeEach
        void setUp() {
            defaultSchedule = expressions.eventually();
        }

        @Test
        void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, PREDICATE);
                will(returnValue(true));
            }});

            expressions.waitUntil(SUBJECT, PREDICATE);
        }

        @Test
        void throwsPollTimeoutException_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.waitUntil(SUBJECT, PREDICATE)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(expressions.eventually(), SUBJECT, PREDICATE)));
        }
    }

    static class WaitUntilWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(true));
            }});

            expressions.waitUntil(schedule, SUBJECT, PREDICATE);
        }

        @Test
        void throwsPollTimeoutException_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.waitUntil(schedule, SUBJECT, PREDICATE)
            );

            assertThat(thrown.getMessage(), is(Diagnosis.of(schedule, SUBJECT, PREDICATE)));
        }
    }

    static class WhenWithDefaultPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule defaultSchedule;

        @BeforeEach
        void setUp() {
            defaultSchedule = expressions.eventually();
        }

        @Test
        void returnsSubject_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, PREDICATE);
                will(returnValue(true));
            }});

            String returnedValue = expressions.when(SUBJECT, PREDICATE);
            assertThat(returnedValue, is(sameInstance(SUBJECT)));
        }

        @Test
        void throwsPollTimeoutException_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.when(SUBJECT, PREDICATE)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(expressions.eventually(), SUBJECT, PREDICATE)));
        }
    }

    static class WhenWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        void returnsSubject_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(true));
            }});

            String returnedValue = expressions.when(schedule, SUBJECT, PREDICATE);
            assertThat(returnedValue, is(sameInstance(SUBJECT)));
        }

        @Test
        void throwsPollTimeoutException_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.when(schedule, SUBJECT, PREDICATE)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(schedule, SUBJECT, PREDICATE)));
        }
    }
}
