package com.dhemery.express.helpers;

import com.dhemery.express.PollEvaluationResult;
import com.dhemery.express.PolledExpressions;
import com.dhemery.express.PollingSchedule;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

public class DissatisfiedPolledExpressions implements PolledExpressions {
    @Override
    public <C extends SelfDescribing & BooleanSupplier> boolean poll(PollingSchedule schedule, C supplier) {
        return false;
    }

    @Override
    public <T, P extends SelfDescribing & Predicate<? super T>> boolean poll(PollingSchedule schedule, T subject, P predicate) {
        return false;
    }

    @Override
    public <T, R, F extends SelfDescribing & Function<? super T, R>> PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
        return new PollEvaluationResult<>(function.apply(subject), false);
    }

    @Override
    public <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>> PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
        return new PollEvaluationResult<>(function.apply(subject), false);
    }
}
