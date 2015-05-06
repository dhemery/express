package com.dhemery.express;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

/**
 * A polling schedule with a given interval and a default duration.
 * <p>
 * This class is not intended for direct use. Instead, call {@link
 * Timeframes#checkingEvery}.
 */
public class CheckingEvery extends PollingSchedule {

    /**
     * Creates a polling schedule with the given interval and duration.
     * <p>
     * This constructor is not intended for direct use. Instead, call {@link
     * Timeframes#checkingEvery}.
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
     * Creates a polling schedule with the given duration and this schedule's
     * interval.
     *
     * @param duration
     *         the duration to poll
     *
     * @return a polling schedule with the given duration and this schedule's
     * interval
     */
    public PollingSchedule expiringIn(Duration duration) {
        return new PollingSchedule(interval(), duration);
    }

    /**
     * Creates a polling schedule with the specified duration and this schedule's
     * interval.
     *
     * @param amount
     *         the duration to poll, measured in terms of the unit
     * @param unit
     *         the unit that the polling duration is measured in
     *
     * @return a polling schedule with the specified duration and this schedule's
     * interval
     */
    public PollingSchedule expiringIn(int amount, TemporalUnit unit) {
        return expiringIn(Duration.of(amount, unit));
    }
}
