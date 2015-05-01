package com.dhemery.express;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

public interface PollTimerPoller extends Poller {
    @Override
    default <C extends SelfDescribing & BooleanSupplier>
    boolean poll(PollingSchedule schedule, C supplier) {
        PollTimer timer = pollTimer();
        timer.start();
        while (!timer.isExpired()) {
            if (supplier.getAsBoolean()) return true;
            timer.tick();
        }
        return false;
    }

    PollTimer pollTimer();

    @Override
    default <T, P extends SelfDescribing & Predicate<? super T>>
    boolean poll(PollingSchedule schedule, T subject, P predicate) {
        return false;
    }

    @Override
    default <T, R, F extends SelfDescribing & Function<? super T, R>>
    PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
        return null;
    }

    @Override
    default <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
    PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
        return null;
    }
}
