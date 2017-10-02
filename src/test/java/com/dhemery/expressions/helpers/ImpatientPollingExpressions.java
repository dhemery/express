package com.dhemery.expressions.helpers;

import com.dhemery.expressions.PolledExpressions;
import com.dhemery.expressions.PollingSchedule;
import com.dhemery.expressions.polling.PollEvaluationResult;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

public class ImpatientPollingExpressions implements PolledExpressions {
    @Override
    public PollingSchedule eventually() {
        return PollingSchedules.rightNow();
    }

    @Override
    public boolean poll(PollingSchedule schedule, BooleanSupplier supplier) {
        return supplier.getAsBoolean();
    }

    @Override
    public <T> boolean poll(PollingSchedule schedule, T subject, Predicate<? super T> predicate) {
        return predicate.test(subject);
    }

    @Override
    public <T, V> PollEvaluationResult<V> poll(PollingSchedule schedule, T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        V value = function.apply(subject);
        return new PollEvaluationResult<>(value, predicate.test(value));
    }
}
