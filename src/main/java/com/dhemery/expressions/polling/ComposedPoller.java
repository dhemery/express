package com.dhemery.expressions.polling;

import com.dhemery.expressions.Poller;
import com.dhemery.expressions.PollingSchedule;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

public class ComposedPoller implements Poller {
    private final Runnable onStart;
    private final BooleanSupplier continuePolling;
    private final Runnable betweenPolls;

    public ComposedPoller(Runnable onStart, BooleanSupplier continuePolling, Runnable betweenPolls) {
        this.onStart = onStart;
        this.continuePolling = continuePolling;
        this.betweenPolls = betweenPolls;
    }

    @Override
    public boolean poll(BooleanSupplier condition) {
        onStart.run();
        while(!condition.getAsBoolean()) {
            if(!continuePolling.getAsBoolean()) return false;
            betweenPolls.run();
        }
        return true;
    }

    @Override
    public boolean poll(PollingSchedule schedule, BooleanSupplier supplier) {
        return false;
    }

    @Override
    public <T> boolean poll(PollingSchedule schedule, T subject, Predicate<? super T> predicate) {
        return false;
    }

    @Override
    public <T, V> PollEvaluationResult<V> poll(PollingSchedule schedule, T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        return null;
    }
}
