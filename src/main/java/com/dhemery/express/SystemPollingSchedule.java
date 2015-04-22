package com.dhemery.express;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.IllegalFormatException;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.format;

/**
 * A polling schedule with interval and duration defined by system properties.
 * If the system properties omit the interval property, the default interval is 1 second.
 * If the system properties omit the duration property, the default duration is 1 minute.
 */
// This class is protected to prevent populating the instance before
// Poller.eventually() asks for it.
class SystemPollingSchedule extends PollingSchedule {
    private static final String DURATION_PROPERTY = "com.dhemery.express.polling.duration.millis";
    private static final String INTERVAL_PROPERTY = "com.dhemery.express.polling.interval.millis";

    static PollingSchedule INSTANCE = new SystemPollingSchedule();

    private SystemPollingSchedule() {
        super(durationFromProperty(INTERVAL_PROPERTY).orElse(Duration.of(1, ChronoUnit.SECONDS)),
                durationFromProperty(DURATION_PROPERTY).orElse(Duration.of(1, ChronoUnit.MINUTES)));
    }

    private static Optional<Duration> durationFromProperty(String name) {
        String value = System.getProperty(name);
        try {
            return Optional.of(value)
                    .map(Integer::valueOf)
                    .map(i -> Duration.of(i, ChronoUnit.MILLIS));
        } catch (NumberFormatException cause) {
            throw new RuntimeException(format("Could not parse an integer the system property: %s%n     The property value was: %s", name, value), cause);
        }
    }
}
