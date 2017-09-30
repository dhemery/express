package com.dhemery.expressions;

import java.time.Duration;

import static java.lang.String.format;

/**
 * The interval and duration for a poll.
 */
public class PollingSchedule {
    private final Duration interval;
    private final Duration duration;

    /**
     * Create a schedule to poll with the given interval and duration.
     *
     * @param interval the polling interval
     * @param duration the duration to poll
     */
    public PollingSchedule(Duration interval, Duration duration) {
        this.interval = interval;
        this.duration = duration;
    }

    /**
     * Returns this schedule's polling duration.
     *
     * @return this schedule's polling duration
     */
    public Duration duration() {
        return duration;
    }

    /**
     * Returns this schedule's polling interval.
     *
     * @return this schedule's polling interval
     */
    public Duration interval() {
        return interval;
    }

    @Override
    public String toString() {
        return format("every %s for %s", interval, duration);
    }

    /**
     * @return {@code true} if the given object is a {@code PollingSchedule}
     * with the same interval and duration as this schedule.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PollingSchedule)) return false;

        PollingSchedule that = (PollingSchedule) o;

        return (interval != null ? interval.equals(that.interval) : that.interval == null)
                && !(duration != null ? !duration.equals(that.duration) : that.duration != null);

    }

    @Override
    public int hashCode() {
        int result = interval != null ? interval.hashCode() : 0;
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }
}
