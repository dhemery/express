package com.dhemery.expressions.polling;

import com.dhemery.expressions.polling.ClockPollTimer;
import com.dhemery.expressions.polling.PollTimer;
import com.dhemery.expressions.polling.PollingSchedule;
import com.dhemery.expressions.polling.Sleeper;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.auto.Auto;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ClockPollTimerTests {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Auto States sleeperType;
    @Mock
    Sleeper sleeper;

    private final ManualClock clock = new ManualClock();
    private PollTimer timer;

    @Before
    public void setup() {
        context.checking(new Expectations() {{
            allowing(sleeper).sleep(with(any(Duration.class)));
            when(sleeperType.is("default"));
            will(advanceTheClockByTheDuration());
        }});

        sleeperType.become("default");
        timer = new ClockPollTimer(clock, sleeper);
    }

    @Test
    public void withNegativePollDuration_isExpiredOnStart() {
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofMinutes(-1));
        timer.start(schedule);
        assertThat(timer.isExpired(), is(true));
    }

    @Test
    public void withZeroPollDuration_isExpiredOnStart() {
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofMinutes(0));
        timer.start(schedule);
        assertThat(timer.isExpired(), is(true));
    }

    @Test
    public void withPositivePollDuration_isNotExpiredOnStart() {
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofMinutes(1));
        timer.start(schedule);
        assertThat(timer.isExpired(), is(false));
    }

    @Test
    public void isNotExpired_ifTimeElapsedSinceStart_isLessThanPollDuration() {
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(3).plus(Duration.ofNanos(1)));
        timer.start(schedule);
        timer.tick();
        timer.tick();
        timer.tick();
        assertThat(timer.isExpired(), is(false));
    }

    @Test
    public void isExpired_ifTimeElapsedSinceStart_isExactlyPollDuration() {
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(3));
        timer.start(schedule);
        timer.tick();
        timer.tick();
        timer.tick();
        assertThat(timer.isExpired(), is(true));
    }

    @Test
    public void isExpired_ifTimeElapsedSinceStart_exceedsPollDuration() {
        PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(3).minus(Duration.ofNanos(1)));
        timer.start(schedule);
        timer.tick();
        timer.tick();
        timer.tick();
        assertThat(timer.isExpired(), is(true));
    }

    @Test
    public void tick_sleepsUsingSleeper() {
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
    public void tick_sleepsUntilNextPollingIntervalBoundary() {
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

        public void advance(Duration amountToAdvance) {
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
