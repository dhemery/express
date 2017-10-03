package com.dhemery.expressions.helpers;

import com.dhemery.expressions.Poller;
import com.dhemery.expressions.PollingSchedule;
import com.dhemery.expressions.polling.PollEvaluationResult;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

public class ImpatientPoller implements Poller {
    @Override
    public boolean poll(PollingSchedule ignored, BooleanSupplier supplier) {
        return supplier.getAsBoolean();
    }

    @Override
    public <T> boolean poll(PollingSchedule ignored, T subject, Predicate<? super T> predicate) {
        return predicate.test(subject);
    }

    @Override
    public <T, V> PollEvaluationResult<V> poll(PollingSchedule ignored, T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        V value = function.apply(subject);
        return new PollEvaluationResult<>(value, predicate.test(value));
    }
}
