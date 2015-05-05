package com.dhemery.express;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static java.lang.String.format;

/**
 * A singleton polling schedule with the default polling interval and duration.
 * <p>
 * The default polling interval is specified (in milliseconds)
 * by the {@link System#getProperties() system property}
 * with the key:
 * <pre>
 *     com.dhemery.express.polling.interval.millis
 * </pre>
 * If the system has no such property, the default
 * polling interval is 1 second.
 * <p>
 * The default polling duration is specified (in milliseconds)
 * by the system property with the key:
 * <pre>
 *     com.dhemery.express.polling.interval.millis
 * </pre>
 * If the system has no such property, the default
 * polling duration is 1 minute.
 */
public class DefaultPollingSchedule extends PollingSchedule {
    /**
     * The key for the system property that defines this schedule's default polling duration.
     */
    public static final String DURATION_PROPERTY = "com.dhemery.express.polling.duration.millis";

    /**
     * The key for the system property that defines this schedule's default polling interval.
     */
    public static final String INTERVAL_PROPERTY = "com.dhemery.express.polling.interval.millis";

    /**
     * The polling duration to use if the system properties do not specify a default.
     */
    public static final Duration FALLBACK_POLLING_DURATION = Duration.of(1, ChronoUnit.MINUTES);

    /**
     * The polling interval to use if the system properties do not specify a default.
     */
    public static final Duration FALLBACK_POLLING_INTERVAL = Duration.of(1, ChronoUnit.SECONDS);

    /**
     * The singleton instance of this default polling schedule.
     */
    public static final PollingSchedule INSTANCE = new DefaultPollingSchedule();

    private DefaultPollingSchedule() {
        super(durationFromProperty(INTERVAL_PROPERTY).orElse(FALLBACK_POLLING_INTERVAL),
                durationFromProperty(DURATION_PROPERTY).orElse(FALLBACK_POLLING_DURATION));
    }

    private static Optional<Duration> durationFromProperty(String name) {
        String value = System.getProperty(name);
        try {
            return Optional.ofNullable(value)
                    .map(Integer::valueOf)
                    .map(i -> Duration.of(i, ChronoUnit.MILLIS));
        } catch (NumberFormatException cause) {
            throw new RuntimeException(
                    String.join(System.lineSeparator(),
                            "Expected an integer (milliseconds)",
                            "   for system property   : " + name,
                            "   but property value was: " + value));
        }
    }
}
