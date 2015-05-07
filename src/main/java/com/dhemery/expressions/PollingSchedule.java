package com.dhemery.expressions;

import java.time.Duration;

import static java.lang.String.format;

/**
 * Defines the interval and duration for a poll.
 */
public class PollingSchedule {
    private final Duration interval;
    private final Duration duration;

    /**
     * Create a schedule to poll with the given interval and duration.
     *
     * @param interval
     *         the polling interval
     * @param duration
     *         the duration to poll
     */
    public PollingSchedule(Duration interval, Duration duration) {
        this.interval = interval;
        this.duration = duration;
    }

    /**
     * Returns this schedule's polling duration.
     * @return this schedule's polling duration
     */
    public Duration duration() {
        return duration;
    }

    /**
     * Returns this schedule's polling interval.
     * @return this schedule's polling interval
     */
    public Duration interval() {
        return interval;
    }

    @Override
    public String toString() {
        return format("every %s for %s", interval, duration);
    }
}
