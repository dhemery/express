package com.dhemery.expressions.polling;

import com.dhemery.expressions.PollingSchedule;
import com.dhemery.expressions.helpers.ManualClock;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClockPollTimerTests {
    private final ManualClock clock = new ManualClock();
    private final PollTimer timer = new ClockPollTimer(clock, null);

    @Nested
    class Start {
        @Test
        void expiredIfPollDurationIsNegative() {
            PollingSchedule polingScheduleWithNegativeDuration = new PollingSchedule(Duration.ofSeconds(1), Duration.ofMinutes(-1));
            timer.start(polingScheduleWithNegativeDuration);
            assertTrue(timer.isExpired());
        }

        @Test
        void expiredIfPollDurationIsZero() {
            PollingSchedule pollingScheduleWithZeroDuration = new PollingSchedule(Duration.ofSeconds(1), Duration.ofMinutes(0));
            timer.start(pollingScheduleWithZeroDuration);
            assertTrue(timer.isExpired());
        }

        @Test
        void notExpiredIfPollDurationIsPositive() {
            PollingSchedule pollingScheduleWithPositiveDuration = new PollingSchedule(Duration.ofSeconds(1), Duration.ofMinutes(1));
            timer.start(pollingScheduleWithPositiveDuration);
            assertFalse(timer.isExpired());
        }
    }

    @Nested
    class Tick {
        @Disabled("needs new implementation")
        @Test
        void notExpiredIfTimeElapsedSinceStartIsLessThanPollDuration() {
            PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(3).plus(Duration.ofNanos(1)));
            timer.start(schedule);
            timer.tick();
            timer.tick();
            timer.tick();
            assertFalse(timer.isExpired());
        }

        @Disabled("needs new implementation")
        @Test
        void expiredIfTimeElapsedSinceStartIsExactlyPollDuration() {
            PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(3));
            timer.start(schedule);
            timer.tick();
            timer.tick();
            timer.tick();
            assertTrue(timer.isExpired());
        }

        @Disabled("needs new implementation")
        @Test
        void expiredIfTimeElapsedSinceStartExceedsPollDuration() {
            PollingSchedule schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(3).minus(Duration.ofNanos(1)));
            timer.start(schedule);
            timer.tick();
            timer.tick();
            timer.tick();
            assertTrue(timer.isExpired());
        }

        @Disabled("needs new implementation")
        @Test
        void sleepsUsingSleeper() {
            final Duration pollingInterval = Duration.ofSeconds(1);
            PollingSchedule schedule = new PollingSchedule(pollingInterval, Duration.ofSeconds(3));

            timer.start(schedule);
            timer.tick();
            timer.tick();
            timer.tick();
        }

        @Disabled("needs new implementation")
        @Test
        void tick_sleepsUntilNextPollingIntervalBoundary() {
            final Duration pollingInterval = Duration.ofSeconds(1);
            PollingSchedule schedule = new PollingSchedule(pollingInterval, Duration.ofSeconds(3));
            Duration delayBetweenStartAndTick = Duration.ofMillis(123);
            Duration expectedSleepDuration = pollingInterval.minus(delayBetweenStartAndTick);

            timer.start(schedule);
            clock.advance(delayBetweenStartAndTick);
            timer.tick();
        }
    }

}
