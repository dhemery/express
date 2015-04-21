package com.dhemery.express;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * A polling schedule with interval and duration defined by system properties.
 */
public class SystemPollingSchedule extends PollingSchedule {
    /**
     * The name of the polling duration system property.
     */
    public static final String DURATION_PROPERTY = "com.dhemery.express.polling.duration.millis";

    /**
     * The name of the polling interval system property.
     */
    public static final String INTERVAL_PROPERTY = "com.dhemery.express.polling.interval.millis";

    /**
     * The polling schedule with an interval and duration determined by the
     * values of system properties.
     */
    public static PollingSchedule INSTANCE = new SystemPollingSchedule();

    private SystemPollingSchedule() {
        super(durationFromProperty(INTERVAL_PROPERTY).orElse(Duration.of(1, ChronoUnit.SECONDS)),
                durationFromProperty(DURATION_PROPERTY).orElse(Duration.of(1, ChronoUnit.MINUTES)));
    }

    private static Optional<Duration> durationFromProperty(String name) {
        return Optional.of(System.getProperty(name))
                .map(Integer::valueOf)
                .map(i -> Duration.of(i, ChronoUnit.MILLIS));
    }
}
