package com.dhemery.expressions.polling;

import com.dhemery.expressions.PollingSchedule;
import com.dhemery.expressions.helpers.ManualClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ClockPollTimerTests {
    private ManualClock clock;
    private PollTimer timer;

    @Nested
    class Start {
        @BeforeEach
        void createTimer() {
            clock = new ManualClock();
            timer = new ClockPollTimer(clock);
        }

        @Test
        void expiredIfPollDurationIsNegative() {
            PollingSchedule pollingScheduleWithNegativeDuration = new PollingSchedule(Duration.ofSeconds(1), Duration.ofNanos(-1));

            timer.start(pollingScheduleWithNegativeDuration);

            assertTrue(timer.isExpired());
        }

        @Test
        void expiredIfPollDurationIsZero() {
            PollingSchedule pollingScheduleWithZeroDuration = new PollingSchedule(Duration.ofSeconds(1), Duration.ofNanos(0));

            timer.start(pollingScheduleWithZeroDuration);

            assertTrue(timer.isExpired());
        }

        @Test
        void notExpiredIfPollDurationIsPositive() {
            PollingSchedule pollingScheduleWithPositiveDuration = new PollingSchedule(Duration.ofSeconds(1), Duration.ofNanos(1));

            timer.start(pollingScheduleWithPositiveDuration);

            assertFalse(timer.isExpired());
        }
    }

    @Nested
    class IsExpired {
        private PollingSchedule schedule;

        @BeforeEach
        void startTimer() {
            clock = new ManualClock();
            timer = new ClockPollTimer(clock);
            schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(100));
            timer.start(schedule);
        }


        @Test
        void falseIfTimeElapsedSinceStartIsLessThanPollDuration() {
            clock.advance(schedule.duration().minusNanos(1));

            assertFalse(timer.isExpired());
        }

        @Test
        void expiredIfTimeElapsedSinceStartIsExactlyPollDuration() {
            clock.advance(schedule.duration());

            assertTrue(timer.isExpired());
        }

        @Test
        void expiredIfTimeElapsedSinceStartExceedsPollDuration() {
            clock.advance(schedule.duration().plusNanos(1));

            assertTrue(timer.isExpired());
        }
    }

    @Nested
    class Tick {
        private PollingSchedule schedule;
        private Sleeper sleeper;

        @BeforeEach
        void startTimer() {
            schedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(100));
            clock = new ManualClock();
            sleeper = clock::advance;
            timer = new ClockPollTimer(clock, sleeper);
            timer.start(schedule);
        }

        @Test
        void sleepsForPollingInterval() throws Throwable {
            // Simulate the passage of some amount of time
            clock.advance(Duration.ofSeconds(222));

            Instant expectedWakeTime = clock.instant().plus(schedule.interval());

            timer.tick();

            assertEquals(expectedWakeTime, clock.instant());
        }
    }
}
