package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import com.dhemery.expressions.helpers.ExpressionsPolledBy;
import com.dhemery.expressions.helpers.ImpatientPoller;
import com.dhemery.expressions.helpers.PollingSchedules;
import com.dhemery.expressions.polling.PollTimeoutException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class SubjectPredicatePolledExpressionTests {
    private static final String SUBJECT = "subject";
    private static final Predicate<String> SATISFIED_PREDICATE = Named.predicate("equal to", s -> Objects.equals(s, s));
    private static final Predicate<String> UNSATISFIED_PREDICATE = SATISFIED_PREDICATE.negate();
    private static final PollingSchedule IGNORED_POLLING_SCHEDULE = null;
    private final PollingSchedule defaultPollingSchedule = PollingSchedules.random();
    private final PolledExpressions expressions = new ExpressionsPolledBy(new ImpatientPoller(), defaultPollingSchedule);

    @Nested
    class AssertThat {
        @Test
        void returnsIfPollReturnsTrue() {
            expressions.assertThat(IGNORED_POLLING_SCHEDULE, SUBJECT, SATISFIED_PREDICATE);
        }

        @Test
        void throwsAssertionErrorIfPollReturnsFalse() {
            PollingSchedule pollingSchedule = PollingSchedules.random();

            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> expressions.assertThat(pollingSchedule, SUBJECT, UNSATISFIED_PREDICATE)
            );
            assertEquals(Diagnosis.of(pollingSchedule, SUBJECT, UNSATISFIED_PREDICATE), thrown.getMessage());
        }
    }

    @Nested
    class SatisfiedThat {
        @ParameterizedTest
        @CsvSource({"true", "false"})
        void returnsPollResult(boolean pollResult) {
            assertEquals(pollResult, expressions.satisfiedThat(IGNORED_POLLING_SCHEDULE, SUBJECT, t -> pollResult));
        }
    }

    @Nested
    class WaitUntil {
        @Nested
        class WithDefaultPollingSchedule {
            @Test
            void returnsIfPollReturnsTrue() {
                expressions.waitUntil(SUBJECT, SATISFIED_PREDICATE);
            }

            @Test
            void throwsPollTimeoutExceptionIfPollReturnsFalse() {
                PollTimeoutException thrown = assertThrows(
                        PollTimeoutException.class,
                        () -> expressions.waitUntil(SUBJECT, UNSATISFIED_PREDICATE)
                );
                assertEquals(Diagnosis.of(defaultPollingSchedule, SUBJECT, UNSATISFIED_PREDICATE), thrown.getMessage());
            }
        }

        @Nested
        class WithExplicitPollingSchedule {
            private final PollingSchedule explicitPollingSchedule = PollingSchedules.random();

            @Test
            void returnsIfPollReturnsTrue() {
                expressions.waitUntil(explicitPollingSchedule, SUBJECT, SATISFIED_PREDICATE);
            }

            @Test
            void throwsPollTimeoutExceptionIfPollReturnsFalse() {
                PollTimeoutException thrown = assertThrows(
                        PollTimeoutException.class,
                        () -> expressions.waitUntil(explicitPollingSchedule, SUBJECT, UNSATISFIED_PREDICATE)
                );

                assertEquals(Diagnosis.of(explicitPollingSchedule, SUBJECT, UNSATISFIED_PREDICATE), thrown.getMessage());
            }
        }
    }

    @Nested
    class When {
        @Nested
        class WithDefaultPollingSchedule {
            @Test
            void returnsSubjectIfPollReturnsTrue() {
                assertSame(SUBJECT, expressions.when(SUBJECT, SATISFIED_PREDICATE));
            }

            @Test
            void throwsPollTimeoutExceptionIfPollReturnsFalse() {
                PollTimeoutException thrown = assertThrows(
                        PollTimeoutException.class,
                        () -> expressions.when(SUBJECT, UNSATISFIED_PREDICATE)
                );
                assertEquals(Diagnosis.of(defaultPollingSchedule, SUBJECT, UNSATISFIED_PREDICATE), thrown.getMessage());
            }
        }

        @Nested
        class WithExplicitPollingSchedule {
            private final PollingSchedule explicitPollingSchedule = PollingSchedules.random();

            @Test
            void returnsSubjectIfPollReturnsTrue() {
                assertSame(SUBJECT, expressions.when(explicitPollingSchedule, SUBJECT, SATISFIED_PREDICATE));
            }

            @Test
            void throwsPollTimeoutExceptionIfPollReturnsFalse() {
                PollTimeoutException thrown = assertThrows(
                        PollTimeoutException.class,
                        () -> expressions.when(explicitPollingSchedule, SUBJECT, UNSATISFIED_PREDICATE)
                );
                assertEquals(Diagnosis.of(explicitPollingSchedule, SUBJECT, UNSATISFIED_PREDICATE), thrown.getMessage());
            }
        }
    }
}