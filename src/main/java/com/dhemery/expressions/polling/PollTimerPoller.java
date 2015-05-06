package com.dhemery.expressions.polling;

import com.dhemery.expressions.Poller;
import com.dhemery.expressions.SelfDescribingBooleanSupplier;
import com.dhemery.expressions.SelfDescribingFunction;
import com.dhemery.expressions.SelfDescribingPredicate;
import org.hamcrest.Matcher;

/**
 * A poller that uses a {@link PollTimer} to pause between evaluations and
 * to determine whether the schedule has expired.
 */
public interface PollTimerPoller extends Poller {
    @Override
    default boolean poll(PollingSchedule schedule, SelfDescribingBooleanSupplier supplier) {
        PollTimer timer = pollTimer();
        timer.start(schedule);
        while (true) {
            if (timer.isExpired()) return false;
            if (supplier.getAsBoolean()) return true;
            timer.tick();
        }
    }

    /**
     * Returns a newly created poll timer to guide one poll.
     *
     * @return a newly created poll timer
     *
     * @implNote returns a new {@link ClockPollTimer}.
     */
    default PollTimer pollTimer() {
        return new ClockPollTimer();
    }

    @Override
    default <T> boolean poll(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        return false;
    }

    @Override
    default <T, V> PollEvaluationResult<V> poll(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher) {
        return null;
    }

    @Override
    default <T, V> PollEvaluationResult<V> poll(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate) {
        return null;
    }
}
