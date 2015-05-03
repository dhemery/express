package com.dhemery.express.helpers;

import com.dhemery.express.*;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

public class ExpressionsPolledBy implements PolledExpressions {
    private Poller poller;
    private Eventually eventually;

    public ExpressionsPolledBy(Poller poller) {
        this(poller, new Timeframes() {
        });
    }

    public ExpressionsPolledBy(Poller poller, Eventually eventually) {
        this.poller = poller;
        this.eventually = eventually;
    }

    @Override
    public <C extends SelfDescribing & BooleanSupplier> boolean poll(PollingSchedule schedule, C supplier) {
        return poller.poll(schedule, supplier);
    }

    @Override
    public <T, P extends SelfDescribing & Predicate<? super T>> boolean poll(PollingSchedule schedule, T subject, P predicate) {
        return poller.poll(schedule, subject, predicate);
    }

    @Override
    public <T, R, F extends SelfDescribing & Function<? super T, R>> PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
        return poller.poll(schedule, subject, function, matcher);
    }

    @Override
    public <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>> PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
        return poller.poll(schedule, subject, function, predicate);
    }

    @Override
    public PollingSchedule eventually() {
        return eventually.eventually();
    }
}
