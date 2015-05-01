package com.dhemery.express;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class ClockPollerTests {
    public static class ScheduleWithBooleanSupplier {
        @Rule
        public JUnitRuleMockery context = new JUnitRuleMockery();
        private final Poller poller = new ClockPoller() {
        };

        @Test
        public void scheduleWithBooleanSupplier_returnsTrueIfSupplierImmediatelyReturnsTrue() {
            SelfDescribingBooleanSupplier supplier = context.mock(SelfDescribingBooleanSupplier.class);

            context.checking(new Expectations() {{
                oneOf(supplier).getAsBoolean();
                will(returnValue(true));
            }});

            assertThat(poller.poll(null, supplier), is(true));
        }
    }
}
