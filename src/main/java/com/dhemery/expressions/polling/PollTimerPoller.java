package com.dhemery.expressions.polling;

import com.dhemery.expressions.Poller;
import com.dhemery.expressions.PollingSchedule;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A poller that uses a {@link PollTimer} to pause between evaluations and
 * to determine whether the schedule has expired.
 */
public interface PollTimerPoller extends Poller {
    @Override
    default boolean poll(PollingSchedule schedule, BooleanSupplier supplier) {
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
    default <T> boolean poll(PollingSchedule schedule, T subject, Predicate<? super T> predicate) {
        return false;
    }

    @Override
    default <T, V> PollEvaluationResult<V> poll(PollingSchedule schedule, T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        return null;
    }
}
