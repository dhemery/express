package com.dhemery.expressions;

import com.dhemery.expressions.polling.DefaultPollingSchedule;
import com.dhemery.expressions.polling.Within;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

/**
 * <p>Factory methods for polling schedules. Each method is named
 * to read like a time frame in a polled expression:
 * <pre>
 * assertThat(<strong>eventually()</strong>, searchField, isDisplayed());
 * assertThat(<strong>within(1, MINUTES)</strong>, searchField, isDisplayed());
 * assertThat(<strong>checkingEvery(1, SECONDS)</strong>, searchField, isDisplayed());
 * </pre>
 */
public interface TimeFrames extends Eventually {
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
     *
     * <p>This factory method is named to read nicely in polled expressions:
     *
     * <pre>
     * assertThat(within(1, MINUTES), searchField, isDisplayed());
     * </pre>
     *
     * To specify a polling interval, call {@link Within#checkingEvery checkingEvery}
     * on the returned schedule:
     *
     * <pre>
     * assertThat(within(1, MINUTES)<strong>.checkingEvery(1, SECONDS)</strong>, searchField, isDisplayed());
     * </pre>
     *
     * @param amount
     *         the duration to poll, measured in terms of the unit
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
     *
     * <p>This factory method is named to read nicely in polled expressions:
     *
     * <pre>
     * Duration oneMinute = Duration.of(1, MINUTES);
     * assertThat(within(oneMinute), searchField, isDisplayed());
     * </pre>
     *
     * To specify a polling interval, call {@link Within#checkingEvery checkingEvery}
     * on the returned schedule:
     *
     * <pre>
     * assertThat(within(oneMinute).checkingEvery(1, SECONDS), searchField, isDisplayed());
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
     * <p>This factory method is named to read nicely in polled expressions:
     *
     * <pre>
     * assertThat(checkingEvery(1, SECONDS), searchField, isDisplayed());
     * </pre>
     *
     * To specify both a polling interval and duration, see {@link #within}.
     *
     * @param amount
     *         the interval on which to poll, measured in terms of the unit
     * @param unit
     *         the unit that the polling interval is measured in
     *
     * @return a polling schedule with the given interval and default polling duration
     *
     * @implSpec the returned schedule must have the same polling duration as
     * the schedules returned by {@link Eventually#eventually eventually}.
     */
    default PollingSchedule checkingEvery(int amount, TemporalUnit unit) {
        return new PollingSchedule(Duration.of(amount, unit), eventually().duration());
    }

    /**
     * Creates a polling schedule with the given polling interval and the
     * default polling duration.
     *
     * <p>This factory method is named to read nicely in polled expressions:
     *
     * <pre>
     * Duration oneSecond = Duration.of(1, SECONDS);
     * assertThat(checkingEvery(oneSecond), searchField, isDisplayed());
     * </pre>
     *
     * To specify both a polling interval and duration, see {@link #within}.
     *
     * @param interval
     *         the interval on which to poll
     *
     * @return a polling schedule with the given interval and default polling duration
     *
     * @implSpec the returned schedule must have the same polling duration as
     * the schedules returned by {@link Eventually#eventually eventually}.
     */
    default PollingSchedule checkingEvery(Duration interval) {
        return new PollingSchedule(interval, eventually().duration());
    }
}
