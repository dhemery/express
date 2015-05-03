package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionsPolledBy;
import com.dhemery.express.helpers.PolledExpressionTestSetup;
import com.dhemery.express.helpers.PollingSchedules;
import org.hamcrest.SelfDescribing;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.function.BooleanSupplier;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class BooleanSupplierPolledExpressionTests {
    public static final SelfDescribingBooleanSupplier SUPPLIER = Named.booleanSupplier("supplier", () -> true);
    public static final PollingSchedule SCHEDULE = PollingSchedules.random();

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

    public static class WhenWithDefaultSchedule extends PolledExpressionTestSetup {
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

    public static class WaitUntilWithExplicitSchedule extends PolledExpressionTestSetup {
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
