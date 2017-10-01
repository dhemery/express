package com.dhemery.expressions.polling;

import com.dhemery.expressions.PollingSchedule;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ClockPollTimerTests {
    private Mockery context = new JUnit4Mockery();
    private States sleeperType = context.states("poll state");

    private Sleeper sleeper = context.mock(Sleeper.class);

    private final ManualClock clock = new ManualClock();
    private PollTimer timer;

    @BeforeEach
    void setup() {
        context.checking(new Expectations() {{
            allowing(sleeper).sleep(with(any(Duration.class)));
            when(sleeperType.is("default"));
            will(advanceTheClockByTheDuration());
        }});

        sleeperType.become("default");
        timer = new ClockPollTimer(clock, sleeper);
    }

    @Test
    void withNegativePollDuration_isExpiredOnStart() {
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofMinutes(-1));
        timer.start(schedule);
        assertThat(timer.isExpired(), is(true));
    }

    @Test
    void withZeroPollDuration_isExpiredOnStart() {
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofMinutes(0));
        timer.start(schedule);
        assertThat(timer.isExpired(), is(true));
    }

    @Test
    void withPositivePollDuration_isNotExpiredOnStart() {
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofMinutes(1));
        timer.start(schedule);
        assertThat(timer.isExpired(), is(false));
    }

    @Test
    void isNotExpired_ifTimeElapsedSinceStart_isLessThanPollDuration() {
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(3).plus(Duration.ofNanos(1)));
        timer.start(schedule);
        timer.tick();
        timer.tick();
        timer.tick();
        assertThat(timer.isExpired(), is(false));
    }

    @Test
    void isExpired_ifTimeElapsedSinceStart_isExactlyPollDuration() {
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(3));
        timer.start(schedule);
        timer.tick();
        timer.tick();
        timer.tick();
        assertThat(timer.isExpired(), is(true));
    }

    @Test
    void isExpired_ifTimeElapsedSinceStart_exceedsPollDuration() {
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(3).minus(Duration.ofNanos(1)));
        timer.start(schedule);
        timer.tick();
        timer.tick();
        timer.tick();
        assertThat(timer.isExpired(), is(true));
    }

    @Test
    void tick_sleepsUsingSleeper() {
        final Duration pollingInterval = Duration.ofSeconds(1);
        PollingSchedule schedule = new PollingSchedule(pollingInterval, Duration.ofSeconds(3));

        sleeperType.become("special");
        context.checking(new Expectations() {{
            exactly(3).of(sleeper).sleep(with(any(Duration.class)));
        }});

        timer.start(schedule);
        timer.tick();
        timer.tick();
        timer.tick();
    }

    @Test
    void tick_sleepsUntilNextPollingIntervalBoundary() {
        final Duration pollingInterval = Duration.ofSeconds(1);
        PollingSchedule schedule = new PollingSchedule(pollingInterval, Duration.ofSeconds(3));
        Duration delayBetweenStartAndTick = Duration.ofMillis(123);
        Duration expectedSleepDuration = pollingInterval.minus(delayBetweenStartAndTick);

        sleeperType.become("special");
        context.checking(new Expectations() {{
            oneOf(sleeper).sleep(expectedSleepDuration);
            will(advanceTheClockByTheDuration());
        }});

        timer.start(schedule);
        clock.advance(delayBetweenStartAndTick);
        timer.tick();
    }

    public static class ManualClock extends Clock {

        private Instant now = Instant.now();

        @Override
        public ZoneId getZone() {
            return null;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return null;
        }

        @Override
        public Instant instant() {
            return now;
        }

        void advance(Duration amountToAdvance) {
            now = now.plus(amountToAdvance);
        }
    }

    private Action advanceTheClockByTheDuration() {
        return new Action() {
            @Override
            public Object invoke(Invocation invocation) throws Throwable {
                Duration sleepDuration = (Duration) invocation.getParameter(0);
                clock.advance(sleepDuration);
                return null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("advance the clock by the sleep duration");
            }
        };
    }
}
