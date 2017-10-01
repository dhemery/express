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

import java.util.function.BooleanSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BooleanSupplierPolledExpressionTests {
    public static final PollingSchedule SCHEDULE = PollingSchedules.random();
    public static final BooleanSupplier SUPPLIER = Named.booleanSupplier("supplier", () -> true);

    @Nested
    public class AssertThat extends PolledExpressionTestSetup {
        @Test
        public void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUPPLIER);
                will(returnValue(true));
            }});

            expressions.assertThat(SCHEDULE, SUPPLIER);
        }

        @Test
        public void throwsAssertionError_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUPPLIER);
                will(returnValue(false));
            }});

            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> expressions.assertThat(SCHEDULE, SUPPLIER)
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(SCHEDULE, SUPPLIER)));

        }
    }

    @Nested
    class SatisfiedThat extends PolledExpressionTestSetup {
        @Test
        void returnsTrue_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUPPLIER);
                will(returnValue(true));
            }});

            boolean result = expressions.satisfiedThat(SCHEDULE, SUPPLIER);

            assertThat(result, is(true));
        }

        @Test
        void returnsFalse_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUPPLIER);
                will(returnValue(false));
            }});

            boolean result = expressions.satisfiedThat(SCHEDULE, SUPPLIER);

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
