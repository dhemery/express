package com.dhemery.express;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Poll a condition on a schedule until either the condition is satisfied or the
 * schedule expires.
 */
public interface Poller {
    /**
     * Poll the condition on the given schedule.
     *
     * @param schedule
     *         the schedule on which to poll the condition
     * @param condition
     *         the condition to poll
     *
     * @return {@code true} if the condition is satisfied before the polling schedule expires, otherwise {@code false}.
     */
    <C extends SelfDescribing & BooleanSupplier>
    boolean poll(PollingSchedule schedule, C condition);

    <T, P extends SelfDescribing & Predicate<? super T>>
    boolean poll(PollingSchedule schedule, T subject, P predicate);

    <T, R, F extends SelfDescribing & Function<? super T, R>>
    PollSample<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher);

    <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
    PollSample<R> poll(PollingSchedule schedule, T subject, F function, P  predicate);
}
