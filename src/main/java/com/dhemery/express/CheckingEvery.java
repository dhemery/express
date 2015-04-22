package com.dhemery.express;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

/**
 * A polling schedule with a given interval and a default duration.
 * <p>
 * This class is not intended for direct use. Instead, call {@link
 * Poller#checkingEvery(Duration)} or {@link Poller#checkingEvery(int,
 * TemporalUnit)}.
 */
public class CheckingEvery extends PollingSchedule {

    /**
     * Create a polling schedule with the given interval and duration.
     * <p>
     * This constructor is not intended for direct use. Instead, call {@link
     * Poller#checkingEvery(Duration)} or {@link Poller#checkingEvery(int,
     * TemporalUnit)}.
     *
     * @param interval
     *         the interval on which to poll
     * @param duration
     *         the duration to poll
     */
    public CheckingEvery(Duration interval, Duration duration) {
        super(interval, duration);
    }

    /**
     * Create a polling schedule with the given duration and this schedule's
     * interval.
     *
     * @param duration
     *         the duration to poll
     *
     * @return a polling schedule with the given duration and this schedule's
     * interval
     */
    public PollingSchedule expiringAfter(Duration duration) {
        return new PollingSchedule(interval(), duration);
    }
}
