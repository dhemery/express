package com.dhemery.expressions.polling;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

/**
 * A poll timer that tells the time using a {@link Clock} and ticks by calling
 * a {@link Sleeper}.
 */
public class ClockPollTimer implements PollTimer {
    private final Clock realClock;
    private final Sleeper sleeper;

    private Duration interval;
    private Instant expiration;
    private Clock intervalAlignedClock;

    /**
     * Creates a poll timer that tells time using a {@link Clock#systemUTC() system clock}
     * and ticks by putting the current thread to sleep until the next wakeup time.
     */
    public ClockPollTimer() {
        this(Clock.systemUTC());
    }

    /**
     * Creates a poll timer that tells time using the given clock and ticks by
     * putting the thread to sleep until the next wakeup time.
     *
     * @param clock
     *         tells the time
     */
    public ClockPollTimer(Clock clock) {
        this(clock, threadSleeper());
    }

    /**
     * Creates a poll timer that tells time using the given clock and ticks using
     * the given sleeper.
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
        this.realClock = clock;
        this.sleeper = sleeper;
    }

    @Override
    public void start(PollingSchedule schedule) {
        interval = schedule.interval();
        Clock tickingClock = Clock.tick(realClock, interval);
        Instant realClockTime = realClock.instant();
        Duration intervalAlignedClockOffset = Duration.between(tickingClock.instant(), realClockTime);
        intervalAlignedClock = Clock.offset(tickingClock, intervalAlignedClockOffset);
        expiration = realClockTime.plus(schedule.duration());
    }

    @Override
    public boolean isExpired() {
        return realClock.instant().compareTo(expiration) >= 0;
    }

    @Override
    public void tick() {
        Duration sleepDuration = durationUntilNextPoll();
        if (sleepDuration.isNegative()) return;
        sleeper.sleep(sleepDuration);
    }

    private static Sleeper threadSleeper() {
        return sleepDuration -> {
            try {
                Thread.sleep(sleepDuration.toMillis());
            } catch (InterruptedException ignored) {
            }
        };
    }

    private Duration durationUntilNextPoll() {
        Instant wakeupTime = intervalAlignedClock.instant().plus(interval);
        return Duration.between(realClock.instant(), wakeupTime);
    }
}
