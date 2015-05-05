package com.dhemery.express;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;

public interface PollTimerPoller extends Poller {
    @Override
    default boolean poll(PollingSchedule schedule, SelfDescribingBooleanSupplier supplier) {
        PollTimer timer = pollTimer();
        timer.start(schedule);
        while (!timer.isExpired()) {
            if (supplier.getAsBoolean()) return true;
            timer.tick();
        }
        return false;
    }

    PollTimer pollTimer();

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
