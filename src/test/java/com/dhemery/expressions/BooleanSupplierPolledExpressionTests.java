package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.helpers.PolledExpressionTestSetup;
import com.dhemery.expressions.helpers.PollingSchedules;
import com.dhemery.expressions.polling.PollTimeoutException;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import static com.dhemery.expressions.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BooleanSupplierPolledExpressionTests {
    public static final PollingSchedule SCHEDULE = PollingSchedules.random();
    public static final SelfDescribingBooleanSupplier SUPPLIER = Named.booleanSupplier("supplier", () -> true);

    public static class AssertThat extends PolledExpressionTestSetup {
        @Test
        public void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUPPLIER);
                will(returnValue(true));
            }});

            expressions.assertThat(SCHEDULE, SUPPLIER);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUPPLIER);
                will(returnValue(false));
            }});

            expressions.assertThat(SCHEDULE, SUPPLIER);
        }

        @Test
        public void errorMessage_describesScheduleAndSupplier() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUPPLIER);
                will(returnValue(false));
            }});

            String message = messageThrownBy(() -> expressions.assertThat(SCHEDULE, SUPPLIER));

            assertThat(message, is(Diagnosis.of(SCHEDULE, SUPPLIER)));
        }
    }

    public static class SatisfiedThat extends PolledExpressionTestSetup {
        @Test
        public void returnsTrue_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUPPLIER);
                will(returnValue(true));
            }});

            boolean result = expressions.satisfiedThat(SCHEDULE, SUPPLIER);

            assertThat(result, is(true));
        }

        @Test
        public void returnsFalse_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUPPLIER);
                will(returnValue(false));
            }});

            boolean result = expressions.satisfiedThat(SCHEDULE, SUPPLIER);

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
        public void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUPPLIER);
                will(returnValue(true));
            }});

            expressions.waitUntil(SUPPLIER);
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUPPLIER);
                will(returnValue(false));
            }});

            expressions.waitUntil(SUPPLIER);
        }

        @Test
        public void exceptionMessage_describesSupplierAndDefaultSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUPPLIER);
                will(returnValue(false));
            }});

            String message = messageThrownBy(() -> expressions.waitUntil(SUPPLIER));

            assertThat(message, is(Diagnosis.of(defaultSchedule, SUPPLIER)));
        }
    }

    public static class WaitUntilWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUPPLIER);
                will(returnValue(true));
            }});

            expressions.waitUntil(schedule, SUPPLIER);
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUPPLIER);
                will(returnValue(false));
            }});

            expressions.waitUntil(schedule, SUPPLIER);
        }

        @Test
        public void exceptionMessage_describesSupplierAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUPPLIER);
                will(returnValue(false));
            }});

            String message = messageThrownBy(() -> expressions.waitUntil(schedule, SUPPLIER));

            assertThat(message, is(Diagnosis.of(schedule, SUPPLIER)));
        }
    }
}
