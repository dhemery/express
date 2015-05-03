package com.dhemery.express;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

/**
 * Factory methods for polling schedules. Each method is named to read like a timeframe in a polled expression.
 */
public interface Timeframes extends Eventually {
    /**
     * @implNote The interval for the default polling schedule is defined by the system property:
     * <pre>
     *    com.dhemery.express.polling.duration.millis
     * </pre>
     * If the system properties lack a property with that key, the interval for the default polling schedule is 1
     * second.
     * <p>
     * The duration for the default polling schedule is defined by the system property:
     * <pre>
     *    com.dhemery.express.polling.duration.millis
     * </pre>
     * If the system properties lack a property with that key, the duration for the default polling schedule is 1
     * second.
     */
    @Override
    default PollingSchedule eventually() {
        return SystemPollingSchedule.INSTANCE;
    }

    /**
     * Creates a polling schedule with the given duration and the default polling interval.
     * <p>
     * This factory method is named to read nicely in polled expressions:
     * <pre>
     * assertThat(within(10, MINUTES), jethro, is(awake()));
     * </pre>
     * To specify a polling interval, call the returned schedule's {@link Within#checkingEvery(Duration)
     * checkingEvery(Duration)} method:
     * <pre>
     * assertThat(
     *     within(10, MINUTES).checkingEvery(5, SECONDS),
     *     jethro, is(asleep()));
     * </pre>
     *
     * @param amount
     *         the amount of the duration, measured in terms of the unit
     * @param unit
     *         the unit that the duration is measured in
     *
     * @return a polling schedule with the given duration and default interval
     *
     * @implNote delegates to {@link #within(Duration)}.
     */
    default Within within(int amount, TemporalUnit unit) {
        return within(Duration.of(amount, unit));
    }

    /**
     * Creates a polling schedule with the given duration and the default polling interval.
     * <p>
     * This factory method is named to read nicely in polled expressions:
     * <pre>
     * Duration tenMinutes = Duration.of(10, MINUTES);
     * assertThat(within(tenMinutes), jethro, is(feedingChickens()));
     * </pre>
     * To specify a polling interval, call the returned schedule's {@link Within#checkingEvery(Duration)
     * checkingEvery(Duration)} method:
     * <pre>
     * Duration tenMinutes = Duration.of(10, MINUTES);
     * assertThat(
     *     within(tenMinutes).checkingEvery(5, SECONDS),
     *     jethro, is(wearingPants()));
     * </pre>
     *
     * @param duration
     *         the duration to poll
     *
     * @return a polling schedule with the given duration and default interval
     *
     * @implNote retrieves the default polling interval by calling {@link #eventually()}.
     */
    default Within within(Duration duration) {
        return new Within(eventually().interval(), duration);
    }

    /**
     * Creates a polling schedule with the given duration and the default polling interval.
     * <p>
     * This factory method is named to read nicely in polled expressions:
     * <pre>
     * assertThat(within(5, DAYS), jethro, is(eatingPie()));
     * </pre>
     * <p>
     * To specify a polling duration, call the returned schedule's {@link CheckingEvery#expiringAfter(Duration)
     * expiringAfter(Duration)} method:
     * <pre>
     * assertThat(
     *     checkingEvery(1, SECONDS).expiringAfter(2, HOURS),
     *     jethro, is(hungryAgain()));
     * </pre>
     *
     * @param amount
     *         the amount of the polling interval, measured in terms of the unit
     * @param unit
     *         the unit that the polling interval is measured in
     *
     * @return a polling schedule with the given interval and default duration
     *
     * @implNote delegates to {@link #checkingEvery(Duration)}.
     */
    default CheckingEvery checkingEvery(int amount, TemporalUnit unit) {
        return checkingEvery(Duration.of(amount, unit));
    }

    /**
     * Creates a polling schedule with the given polling interval and the default polling duration.
     * <p>
     * This factory method is named to read nicely in polled expressions:
     * <pre>
     * Duration tenMinutes = Duration.of(10, MINUTES);
     * assertThat(checkingEvery(tenMinutes), jethro, is(missingHisMother()));
     * </pre>
     * <p>
     * To specify a polling duration, call the returned schedule's {@link CheckingEvery#expiringAfter(Duration)
     * expiringAfter(Duration)} method:
     * <pre>
     * Duration tenMinutes = Duration.of(10, MINUTES);
     * assertThat(
     *     checkingEvery(tenMinutes).expiringAfter(2, HOURS),
     *     jethro, is(walkingInMemphis()));
     * </pre>
     *
     * @param interval
     *         the interval on which to poll
     *
     * @return a polling schedule with the given interval and default duration
     *
     * @implNote retrieves the default polling duration by calling {@link #eventually()}.
     */
    default CheckingEvery checkingEvery(Duration interval) {
        return new CheckingEvery(interval, eventually().duration());
    }
}
