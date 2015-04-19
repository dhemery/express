package com.dhemery.express;

import java.time.Duration;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.SECONDS;

public class PollingSchedule {
    private final Duration interval;
    private final Duration duration;

    /**
     * Create a schedule to poll with the given interval and duration.
     * @param interval the polling interval
     * @param duration the duration to poll
     */
    public PollingSchedule(Duration interval, Duration duration) {
        this.interval = interval;
        this.duration = duration;
    }

    public Duration duration() { return duration; }
    public Duration interval() { return interval; }

    @Override
    public String toString() {
        return format("every %s for %s", interval, duration);
    }
}
