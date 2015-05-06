package com.dhemery.expressions.helpers;

import com.dhemery.expressions.*;
import org.hamcrest.Matcher;

public class ExpressionsPolledBy implements PolledExpressions {
    private Poller poller;
    private Eventually eventually;

    public ExpressionsPolledBy(Poller poller, Eventually eventually) {
        this.poller = poller;
        this.eventually = eventually;
    }

    @Override
    public boolean poll(PollingSchedule schedule, SelfDescribingBooleanSupplier supplier) {
        return poller.poll(schedule, supplier);
    }

    @Override
    public <T> boolean poll(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        return poller.poll(schedule, subject, predicate);
    }

    @Override
    public <T, V> PollEvaluationResult<V> poll(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher) {
        return poller.poll(schedule, subject, function, matcher);
    }

    @Override
    public <T, V> PollEvaluationResult<V> poll(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate) {
        return poller.poll(schedule, subject, function, predicate);
    }

    @Override
    public PollingSchedule eventually() {
        return eventually.eventually();
    }
}
