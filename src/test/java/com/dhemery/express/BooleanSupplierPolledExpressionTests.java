package com.dhemery.express;

import com.dhemery.express.helpers.PolledExpressionTestSetup;
import com.dhemery.express.helpers.PollingSchedules;
import org.jmock.Expectations;
import org.junit.Test;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
}
