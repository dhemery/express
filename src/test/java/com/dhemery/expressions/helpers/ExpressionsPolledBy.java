package com.dhemery.expressions.helpers;

import com.dhemery.expressions.Eventually;
import com.dhemery.expressions.PolledExpressions;
import com.dhemery.expressions.Poller;
import com.dhemery.expressions.PollingSchedule;
import com.dhemery.expressions.polling.PollEvaluationResult;
import org.hamcrest.Matcher;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

public class ExpressionsPolledBy implements PolledExpressions {
    private Poller poller;
    private Eventually eventually;

    ExpressionsPolledBy(Poller poller, Eventually eventually) {
        this.poller = poller;
        this.eventually = eventually;
    }

    @Override
    public boolean poll(PollingSchedule schedule, BooleanSupplier supplier) {
        return poller.poll(schedule, supplier);
    }

    @Override
    public <T> boolean poll(PollingSchedule schedule, T subject, Predicate<? super T> predicate) {
        return poller.poll(schedule, subject, predicate);
    }

    @Override
    public <T, V> PollEvaluationResult<V> poll(PollingSchedule schedule, T subject, Function<? super T, V> function, Matcher<? super V> matcher) {
        return poller.poll(schedule, subject, function, matcher);
    }

    @Override
    public <T, V> PollEvaluationResult<V> poll(PollingSchedule schedule, T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        return poller.poll(schedule, subject, function, predicate);
    }

    @Override
    public PollingSchedule eventually() {
        return eventually.eventually();
    }
}
