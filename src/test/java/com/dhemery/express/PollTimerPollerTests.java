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
public class PollTimerPollerTests {
    public static class WithBooleanSupplier {
        @Rule
        public JUnitRuleMockery context = new JUnitRuleMockery();
        private final SelfDescribingBooleanSupplier supplier = context.mock(SelfDescribingBooleanSupplier.class);
        private final PollTimer timer = context.mock(PollTimer.class);
        private final Poller poller = new PollTimerPoller() {
            @Override
            public PollTimer pollTimer() {
                return timer;
            }
        };

        @Test
        public void returnsTrue_ifSupplierImmediatelyReturnsTrue() {
            context.checking(new Expectations() {{
                oneOf(supplier).getAsBoolean();
                will(returnValue(true));
            }});

            assertThat(poller.poll(null, supplier), is(true));
        }

        @Test
        public void returnsTrue_ifSupplierReturnsTrue_beforeTimerExpires() {
            context.checking(new Expectations() {{
                atLeast(1).of(supplier).getAsBoolean();
                will(onConsecutiveCalls(
                        returnValue(false),
                        returnValue(false),
                        returnValue(false),
                        returnValue(true)
                ));
                allowing(timer).isExpired();
                will(returnValue(false));
            }});

            assertThat(poller.poll(null, supplier), is(true));
        }

        @Test
        public void returnsFalse_ifTimerExpires_beforeSupplierReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(supplier).getAsBoolean();
                will(returnValue(false));
                atLeast(1).of(timer).isExpired();
                will(onConsecutiveCalls(
                        returnValue(false),
                        returnValue(false),
                        returnValue(false),
                        returnValue(true)
                ));
            }});

            assertThat(poller.poll(null, supplier), is(false));
        }
    }
}
