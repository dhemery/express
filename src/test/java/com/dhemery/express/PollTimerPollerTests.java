package com.dhemery.express;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class PollTimerPollerTests {
    public static class WithBooleanSupplier {
        @Rule
        public JUnitRuleMockery context = new JUnitRuleMockery();
        private final SelfDescribingBooleanSupplier supplier = context.mock(SelfDescribingBooleanSupplier.class);
        private final PollTimer timer = context.mock(PollTimer.class);
        private final PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(10));
        private final Poller poller = new PollTimerPoller() {
            @Override
            public PollTimer pollTimer() {
                return timer;
            }
        };

        @Test
        public void startsTimerWithSchedule_beforeCheckingForExpiration() {
            Sequence polling = context.sequence("polling");

            context.checking(new Expectations() {{ // @formatter:off
                oneOf(timer).start(schedule); inSequence(polling);
                allowing(timer).isExpired(); will(returnValue(true)); inSequence(polling);
            }}); // @formatter:on

            poller.poll(schedule, supplier);
        }

        @Test
        public void returnsFalse_withoutEvaluatingSupplier_ifTimerIsAlreadyExpiredAtStartOfPoll() {
            context.checking(new Expectations() {{
                oneOf(timer).isExpired();
                will(returnValue(true));
                never(supplier);
                allowing(timer);
            }});

            poller.poll(schedule, supplier);
        }

        @Test
        public void returnsTrue_ifSupplierReturnsTrue_beforeTimerExpires() {
            context.checking(new Expectations() {{ // @formatter:off
                atLeast(4).of(supplier).getAsBoolean(); will(onConsecutiveCalls(
                                                                returnValue(false),
                                                                returnValue(false),
                                                                returnValue(false),
                                                                returnValue(true)
                ));
                allowing(timer).isExpired(); will(returnValue(false));
                allowing(timer);
            }}); // @formatter:on

            assertThat(poller.poll(schedule, supplier), is(true));
        }

        @Test
        public void returnsFalse_ifTimerExpires_beforeSupplierReturnsTrue() {
            context.checking(new Expectations() {{ // @formatter:off
                allowing(supplier).getAsBoolean(); will(returnValue(false));
                atLeast(4).of(timer).isExpired(); will(onConsecutiveCalls(
                                                            returnValue(false),
                                                            returnValue(false),
                                                            returnValue(false),
                                                            returnValue(true)
                ));
                allowing(timer);
            }}); // @formatter:on

            assertThat(poller.poll(schedule, supplier), is(false));
        }

        @Test
        public void ticksTimer_betweenEvaluations() {
            Sequence polling = context.sequence("polling");

            context.checking(new Expectations() {{ // @formatter:off
                oneOf(supplier).getAsBoolean(); inSequence(polling); will(returnValue(false));
                oneOf(timer).tick(); inSequence(polling);
                oneOf(supplier).getAsBoolean(); inSequence(polling); will(returnValue(false));
                oneOf(timer).tick(); inSequence(polling);
                oneOf(supplier).getAsBoolean(); inSequence(polling); will(returnValue(false));
                oneOf(timer).tick(); inSequence(polling);
                oneOf(supplier).getAsBoolean(); inSequence(polling); will(returnValue(true));
                allowing(timer).isExpired(); will(returnValue(false));
                allowing(timer);
            }}); // @formatter:on

            poller.poll(schedule, supplier);
        }
    }
}
