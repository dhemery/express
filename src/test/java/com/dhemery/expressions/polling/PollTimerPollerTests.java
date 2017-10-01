package com.dhemery.expressions.polling;

import com.dhemery.expressions.Poller;
import com.dhemery.expressions.PollingSchedule;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.function.BooleanSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PollTimerPollerTests {
    @Nested
    class WithBooleanSupplier {
        Mockery context = new JUnit4Mockery();

        BooleanSupplier supplier = context.mock(BooleanSupplier.class);
        PollTimer timer = context.mock(PollTimer.class);
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(10));
        Poller poller = new PollTimerPoller() {
            @Override
            public PollTimer pollTimer() {
                return timer;
            }
        };

        @Test
        void startsTimerWithSchedule_beforeCheckingForExpiration() {
            States poll = context.states("poll").startsAs("new");

            context.checking(new Expectations() {{
                oneOf(timer).start(schedule);
                when(poll.is("new"));
                then(poll.is("running"));

                allowing(timer).isExpired();
                when(poll.is("running"));
                will(returnValue(true));
            }});

            poller.poll(schedule, supplier);
        }

        @Test
        void returnsFalse_withoutEvaluatingSupplier_ifTimerIsAlreadyExpiredAtStartOfPoll() {
            context.checking(new Expectations() {{
                allowing(timer).isExpired();
                will(returnValue(true));

                never(supplier);
                allowing(timer);
            }});

            poller.poll(schedule, supplier);
        }

        @Test
        void returnsTrue_ifSupplierReturnsTrue_beforeTimerExpires() {
            context.checking(new Expectations() {{
                allowing(supplier).getAsBoolean();
                will(onConsecutiveCalls(
                        returnValue(false),
                        returnValue(false),
                        returnValue(false),
                        returnValue(true)));

                allowing(timer).isExpired();
                will(returnValue(false));

                allowing(timer);
            }});

            assertThat(poller.poll(schedule, supplier), is(true));
        }

        @Test
        void returnsFalse_ifTimerExpires_beforeSupplierReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(supplier).getAsBoolean();
                will(returnValue(false));

                allowing(timer).isExpired();
                will(onConsecutiveCalls(
                        returnValue(false),
                        returnValue(false),
                        returnValue(false),
                        returnValue(true)));

                allowing(timer);
            }});

            assertThat(poller.poll(schedule, supplier), is(false));
        }

        @Test
        void ticksTimer_betweenEvaluations() {
            States poll = context.states("poll").startsAs("evaluating");

            context.checking(new Expectations() {{
                allowing(supplier).getAsBoolean();
                when(poll.is("evaluating"));
                will(onConsecutiveCalls(
                        returnValue(false),
                        returnValue(false),
                        returnValue(false),
                        returnValue(false),
                        returnValue(true)
                ));
                then(poll.is("evaluated"));

                allowing(timer).tick();
                when(poll.is("evaluated"));
                then(poll.is("evaluating"));

                allowing(timer).isExpired();
                will(returnValue(false));

                allowing(timer);
            }});

            poller.poll(schedule, supplier);
        }
    }
}
