package com.dhemery.express;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.function.BooleanSupplier;

/**
 * Poll a condition on a schedule until either the condition is satisfied or the
 * schedule expires.
 */
public interface Poller {
    /**
     * Poll the condition on this poller's default polling schedule.
     *
     * @param condition
     *         the condition to poll
     *
     * @return {@code true} if the condition is satisfied before the polling
     * schedule expires, otherwise {@code false}.
     *
     * @implNote retrieves the default polling schedule by calling {@link
     * #eventually()}.
     */
    default boolean poll(BooleanSupplier condition) {
        return poll(eventually(), condition);
    }

    /**
     * Poll the condition on the given schedule.
     *
     * @param schedule
     *         the schedule on which to poll the condition
     * @param condition
     *         the condition to poll
     *
     * @return {@code true} if the condition is satisfied before the polling
     * schedule expires, otherwise {@code false}.
     */
    boolean poll(PollingSchedule schedule, BooleanSupplier condition);

    /**
     * Return this poller's default polling schedule. This factory method is
     * named to read nicely in polled expressions.
     *
     * @return the default polling schedule
     *
     * @implNote returns {@link SystemPollingSchedule#INSTANCE}.
     * @see PolledExpressions
     */
    default PollingSchedule eventually() {
        return SystemPollingSchedule.INSTANCE;
    }

    /**
     * Create a polling schedule with the given duration and this poller's
     * default polling interval. <p> To specify a polling interval, call the
     * returned schedule's {@link Within#checkingEvery(Duration)
     * checkingEvery(Duration)} method. </p>
     *
     * @param amount
     *         the amount of the duration, measured in terms of the unit
     * @param unit
     *         the unit that the duration is measured in
     *
     * @return the polling schedule
     *
     * @implNote delegates to {@link #within(Duration)}.
     * @see Within
     */
    default Within within(int amount, TemporalUnit unit) {
        return within(Duration.of(amount, unit));
    }

    /**
     * Create a polling schedule with the given duration and this poller's
     * default polling interval. <p> To specify a polling interval, call the
     * returned schedule's {@link Within#checkingEvery(Duration)
     * checkingEvery(Duration)} method. </p>
     *
     * @param duration
     *         the duration to poll
     *
     * @return the polling schedule
     *
     * @implNote retrieves the default polling interval by calling {@link
     * #eventually()}.
     * @see Within
     */
    default Within within(Duration duration) {
        return new Within(eventually().interval(), duration);
    }

    /**
     * Create a polling schedule with the given duration and this poller's
     * default polling interval. <p> To specify a polling duration, call the
     * returned schedule's {@link CheckingEvery#expiringAfter(Duration)
     * expiringAfter(Duration)} method. </p>
     *
     * @param amount
     *         the amount of the polling interval, measured in terms of the
     *         unit
     * @param unit
     *         the unit that the polling interval is measured in
     *
     * @return the polling schedule
     *
     * @implNote delegates to {@link #checkingEvery(Duration)}.
     * @see CheckingEvery
     */
    default CheckingEvery checkingEvery(int amount, TemporalUnit unit) {
        return checkingEvery(Duration.of(amount, unit));
    }

    /**
     * Create a polling schedule with the given polling interval and this
     * poller's default polling duration. <p> To specify a polling duration,
     * call the returned schedule's {@link CheckingEvery#expiringAfter(Duration)
     * expiringAfter(Duration)} method. </p>
     *
     * @param interval
     *         the interval on which to poll
     *
     * @return the polling schedule
     *
     * @implNote retrieves the default polling duration by calling {@link
     * #eventually()}.
     * @see CheckingEvery
     * @see #eventually()
     */
    default CheckingEvery checkingEvery(Duration interval) {
        return new CheckingEvery(interval, eventually().duration());
    }
}
