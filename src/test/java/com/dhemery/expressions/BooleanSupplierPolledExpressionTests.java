package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import com.dhemery.expressions.helpers.ExpressionsPolledBy;
import com.dhemery.expressions.helpers.ImpatientPoller;
import com.dhemery.expressions.helpers.PollingSchedules;
import com.dhemery.expressions.polling.PollTimeoutException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

class BooleanSupplierPolledExpressionTests {
    private static final BooleanSupplier SATISFIED_CONDITION = Named.booleanSupplier("satisfied condition", () -> true);
    private static final BooleanSupplier UNSATISFIED_CONDITION = Named.booleanSupplier("unsatisfied condition", () -> false);
    private static final PollingSchedule IGNORED_POLLING_SCHEDULE = null;
    private final PollingSchedule defaultPollingSchedule = PollingSchedules.random();
    private final PolledExpressions polledExpressions = new ExpressionsPolledBy(new ImpatientPoller(), defaultPollingSchedule);

    @Nested
    class SatisfiedThat {
        @Test
        void returnsTrueIfPollReturnsTrue() {
            assertTrue(polledExpressions.satisfiedThat(IGNORED_POLLING_SCHEDULE, SATISFIED_CONDITION));
        }

        @Test
        void returnsFalseIfPollReturnsFalse() {
            assertFalse(polledExpressions.satisfiedThat(IGNORED_POLLING_SCHEDULE, UNSATISFIED_CONDITION));
        }
    }

    @Nested
    class AssertThat {
        @Test
        void returnsIfPollReturnsTrue() {
            polledExpressions.assertThat(IGNORED_POLLING_SCHEDULE, SATISFIED_CONDITION);
        }

        @Test
        void throwsAssertionErrorIfPollReturnsFalse() {
            PollingSchedule pollingSchedule = PollingSchedules.random();
            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> polledExpressions.assertThat(pollingSchedule, UNSATISFIED_CONDITION)
            );
            assertEquals(Diagnosis.of(pollingSchedule, UNSATISFIED_CONDITION), thrown.getMessage());
        }
    }

    @Nested
    class WaitUntil {
        @Nested
        class WithDefaultPollingSchedule {
            @Test
            void returnsIfPollReturnsTrue() {
                polledExpressions.waitUntil(SATISFIED_CONDITION);
            }

            @Test
            void throwsPollTimeoutExceptionIfPollReturnsFalse() {
                PollTimeoutException thrown = assertThrows(
                        PollTimeoutException.class,
                        () -> polledExpressions.waitUntil(UNSATISFIED_CONDITION)
                );

                assertEquals(Diagnosis.of(defaultPollingSchedule, UNSATISFIED_CONDITION), thrown.getMessage());
            }
        }

        @Nested
        class WithExplicitPollingSchedule {
            private PollingSchedule pollingSchedule = PollingSchedules.random();

            @Test
            void returnsIfPollReturnsTrue() {
                polledExpressions.waitUntil(pollingSchedule, SATISFIED_CONDITION);
            }

            @Test
            void throwsPollTimeoutExceptionIfPollReturnsFalse() {
                PollTimeoutException thrown = assertThrows(
                        PollTimeoutException.class,
                        () -> polledExpressions.waitUntil(pollingSchedule, UNSATISFIED_CONDITION)
                );

                assertEquals(Diagnosis.of(pollingSchedule, UNSATISFIED_CONDITION), thrown.getMessage());
            }
        }
    }
}