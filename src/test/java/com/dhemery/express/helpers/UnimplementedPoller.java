package com.dhemery.express.helpers;

import com.dhemery.express.PollEvaluationResult;
import com.dhemery.express.Poller;
import com.dhemery.express.PollingSchedule;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

public interface UnimplementedPoller extends Poller {
    String UNIMPLEMENTED_OPERATION_MESSAGE = "This method is not implemented for this testable polled expressions";

    @Override
    default <C extends SelfDescribing & BooleanSupplier>
    boolean poll(PollingSchedule schedule, C supplier) {
        throw new RuntimeException(UNIMPLEMENTED_OPERATION_MESSAGE);
    }

    @Override
    default <T, P extends SelfDescribing & Predicate<? super T>>
    boolean poll(PollingSchedule schedule, T subject, P predicate) {
        throw new RuntimeException(UNIMPLEMENTED_OPERATION_MESSAGE);
    }

    @Override
    default <T, R, F extends SelfDescribing & Function<? super T, R>>
    PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
        throw new RuntimeException(UNIMPLEMENTED_OPERATION_MESSAGE);
    }

    @Override
    default <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
    PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
        throw new RuntimeException(UNIMPLEMENTED_OPERATION_MESSAGE);
    }
}
