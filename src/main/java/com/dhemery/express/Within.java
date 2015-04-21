package com.dhemery.express;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

/**
 * A polling schedule that can generate another with the same duration and a
 * different interval.
 * <p>
 * This class is not intended for direct use. Instead, call {@link
 * Poller#within(Duration)} or {@link Poller#within(int, TemporalUnit)}.
 */
public class Within extends PollingSchedule {
    /**
     * Create a polling schedule with the given interval and duration.
     * <p>
     * This constructor is not intended for direct use. Instead, call {@link
     * Poller#within(Duration)} or {@link Poller#within(int, TemporalUnit)}.
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
     * Create a polling schedule with the given interval and this schedule's
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
