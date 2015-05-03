package com.dhemery.express;

import java.time.Duration;

/**
 * A polling schedule with a given duration and a default interval.
 * <p>
 * This class is not intended for direct use. Instead, call {@link
 * Timeframes#within}.
 */
public class Within extends PollingSchedule {
    /**
     * Creates a polling schedule with the given interval and duration.
     * <p>
     * This constructor is not intended for direct use. Instead, call {@link
     * Timeframes#within}.
     *
     * @param interval
     *         the interval on which to poll
     * @param duration
     *         the duration to poll
     */
    public Within(Duration interval, Duration duration) {
        super(interval, duration);
    }

    /**
     * Creates a polling schedule with the given interval and this schedule's
     * duration.
     *
     * @param interval
     *         the interval on which to poll
     *
     * @return a polling schedule with the given duration and this schedule's
     * interval
     */
    public PollingSchedule checkingEvery(Duration interval) {
        return new PollingSchedule(interval, duration());
    }
}
