package com.dhemery.express;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

/**
 * Factory methods to supply polling schedules. Each method is named to read like a
 * timeframe in a polled expression:
 * <pre>
 * assertThat(eventually(), searchField, isDisplayed());
 * assertThat(within(1, MINUTES), searchField, isDisplayed());
 * assertThat(checkingEvery(1, SECONDS), searchField, isDisplayed());
 * </pre>
 */
public interface Timeframes extends Eventually {
    /**
     * @return {@link DefaultPollingSchedule#INSTANCE}.
     */
    @Override
    default PollingSchedule eventually() {
        return DefaultPollingSchedule.INSTANCE;
    }

    /**
     * Creates a polling schedule with the given duration and the default
     * polling interval.
     * <p>
     * This factory method is named to read nicely in polled expressions:
     * <pre>
     * assertThat(within(1, MINUTES), searchField, isDisplayed());
     * </pre>
     * To specify a polling interval, call {@link Within#checkingEvery checkingEvery}
     * on the returned schedule:
     * <pre>
     * assertThat(within(1, MINUTES).checkingEvery(1, SECONDS), searchField, isDisplayed());
     * </pre>
     *
     * @param amount
     *         the amount of the duration, measured in terms of the unit
     * @param unit
     *         the unit that the duration is measured in
     *
     * @return a polling schedule with the given duration and the default polling interval
     *
     * @implSpec the returned schedule must have the same polling interval as
     * the schedules returned by {@link Eventually#eventually eventually}.
     */
    default Within within(int amount, TemporalUnit unit) {
        return within(Duration.of(amount, unit));
    }

    /**
     * Creates a polling schedule with the given duration and the default
     * polling interval.
     * <p>
     * This factory method is named to read nicely in polled expressions:
     * <pre>
     * Duration tenMinutes = Duration.of(1, MINUTES);
     * assertThat(within(tenMinutes), searchField, isDisplayed());
     * </pre>
     * To specify a polling interval, call {@link Within#checkingEvery checkingEvery}
     * on the returned schedule:
     * <pre>
     * assertThat(within(tenMinutes).checkingEvery(1, SECONDS), searchField, isDisplayed());
     * </pre>
     *
     * @param duration
     *         the duration to poll
     *
     * @return a polling schedule with the given duration and default interval
     *
     * @implSpec the returned schedule must have the same polling interval as
     * the schedules returned by {@link Eventually#eventually eventually}.
     */
    default Within within(Duration duration) {
        return new Within(eventually().interval(), duration);
    }

    /**
     * Creates a polling schedule with the given duration and the default
     * polling interval.
     * <p>
     * This factory method is named to read nicely in polled expressions:
     * <pre>
     * assertThat(checkingEvery(1, SECONDS), searchField, isDisplayed());
     * </pre>
     * To specify a polling duration, call {@link CheckingEvery#expiringAfter expiringAfter}
     * on the returned schedule:
     * <pre>
     * assertThat(checkingEvery(1, SECONDS).expiringAfter(1, MINUTES), searchField, isDisplayed());
     * </pre>
     *
     * @param amount
     *         the amount of the polling interval, measured in terms of the
     *         unit
     * @param unit
     *         the unit that the polling interval is measured in
     *
     * @return a polling schedule with the given interval and default duration
     *
     * @implSpec the returned schedule must have the same polling duration as
     * the schedules returned by {@link Eventually#eventually eventually}.
     */
    default CheckingEvery checkingEvery(int amount, TemporalUnit unit) {
        return checkingEvery(Duration.of(amount, unit));
    }

    /**
     * Creates a polling schedule with the given polling interval and the
     * default polling duration.
     * <p>
     * This factory method is named to read nicely in polled expressions:
     * <pre>
     * Duration tenMinutes = Duration.of(10, MINUTES);
     * assertThat(checkingEvery(tenMinutes), searchField, isDisplayed());
     * </pre>
     * To specify a polling duration, call {@link CheckingEvery#expiringAfter expiringAfter}
     * on the returned schedule:
     * CheckingEvery#expiringAfter expiringAfter} method:
     * <pre>
     * Duration oneSecond = Duration.of(1, SECONDS);
     * assertThat(checkingEvery(oneSecond).expiringAfter(1, MINUTES), searchField, isDisplayed());
     * </pre>
     *
     * @param interval
     *         the interval on which to poll
     *
     * @return a polling schedule with the given interval and default duration
     *
     * @implSpec the returned schedule must have the same polling duration as
     * the schedules returned by {@link Eventually#eventually eventually}.
     */
    default CheckingEvery checkingEvery(Duration interval) {
        return new CheckingEvery(interval, eventually().duration());
    }
}
