package com.dhemery.expressions.polling;

import com.dhemery.expressions.PollingSchedule;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

/**
 * A poll timer that tells the time using a {@link Clock} and ticks by calling
 * a {@link Sleeper}.
 */
public class ClockPollTimer implements PollTimer {
    private final Clock clock;
    private final Sleeper sleeper;

    private Duration interval;
    private Instant expiration;

    /**
     * Creates a poll timer that tells time using a {@link Clock#systemUTC() system clock}
     * and ticks using a sleeper that causes the thread to sleep.
     */
    public ClockPollTimer() {
        this(Clock.systemUTC());
    }

    /**
     * Creates a poll timer that tells time using the given clock
     * and ticks using a sleeper that causes the thread to sleep.
     *
     * @param clock
     *         tells the time
     */
    public ClockPollTimer(Clock clock) {
        this(clock, threadSleeper());
    }

    /**
     * Creates a poll timer that tells time using the given clock
     * and ticks using the given sleeper.
     *
     * @param clock
     *         tells the time
     * @param sleeper
     *         sleeps between polls
     *
     * @implNote This constructor exists primarily to allow tests to control
     * the clock and to provide a mechanism for pretending to sleep.
     */
    public ClockPollTimer(Clock clock, Sleeper sleeper) {
        this.clock = clock;
        this.sleeper = sleeper;
    }

    @Override
    public void start(PollingSchedule schedule) {
        interval = schedule.interval();
        expiration = clock.instant().plus(schedule.duration());
    }

    @Override
    public boolean isExpired() {
        return !clock.instant().isBefore(expiration);
    }

    @Override
    public void tick() {
        sleeper.sleep(interval);
    }

    private static Sleeper threadSleeper() {
        return sleepDuration -> {
            try {
                Thread.sleep(sleepDuration.toMillis());
            } catch (InterruptedException ignored) {
            }
        };
    }
}
