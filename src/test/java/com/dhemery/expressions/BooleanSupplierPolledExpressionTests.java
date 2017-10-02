package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import com.dhemery.expressions.helpers.ImpatientPollingExpressions;
import com.dhemery.expressions.helpers.PolledExpressionTestSetup;
import com.dhemery.expressions.helpers.PollingSchedules;
import com.dhemery.expressions.polling.PollTimeoutException;
import org.jmock.Expectations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

import static com.dhemery.expressions.helpers.PollingSchedules.rightNow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class BooleanSupplierPolledExpressionTests {
    private static final BooleanSupplier SUPPLIER = Named.booleanSupplier("supplier", () -> true);
    private static final BooleanSupplier UNSATISFIED_CONDITION = () -> false;
    private static final BooleanSupplier SATISFIED_CONDITION = () -> true;

    private final PolledExpressions polledExpressions = new ImpatientPollingExpressions();

    @Nested
    class SatisfiedThat {
        @Test
        void returnsTrueIfPollReturnsTrue() {
            assertTrue(polledExpressions.satisfiedThat(rightNow(), SATISFIED_CONDITION));
        }

        @Test
        void returnsFalseIfPollReturnsFalse() {
            assertFalse(polledExpressions.satisfiedThat(rightNow(), UNSATISFIED_CONDITION));
        }
    }

    @Nested
    class AssertThat {
        @Test
        void returnsIfPollReturnsTrue() {
            polledExpressions.assertThat(rightNow(), SATISFIED_CONDITION);
        }

        @Test
        void throwsAssertionErrorIfPollReturnsFalse() {
            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> polledExpressions.assertThat(rightNow(), UNSATISFIED_CONDITION)
            );
            assertEquals(Diagnosis.of(rightNow(), UNSATISFIED_CONDITION), thrown.getMessage());
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
        void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUPPLIER);
                will(returnValue(true));
            }});

            expressions.waitUntil(SUPPLIER);
        }

        @Test
        void throwsPollTimeoutException_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUPPLIER);
                will(returnValue(false));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.waitUntil(SUPPLIER)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(defaultSchedule, SUPPLIER)));
        }
    }

    @Nested
    class WaitUntilWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule schedule = PollingSchedules.random();

        @Test
        void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUPPLIER);
                will(returnValue(true));
            }});

            expressions.waitUntil(schedule, SUPPLIER);
        }

        @Test
        void throwsPollTimeoutException_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUPPLIER);
                will(returnValue(false));
            }});

            PollTimeoutException thrown = assertThrows(
                    PollTimeoutException.class,
                    () -> expressions.waitUntil(schedule, SUPPLIER)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(schedule, SUPPLIER)));
        }
    }
}
