package com.dhemery.expressions.polling;

import com.dhemery.expressions.PollingSchedule;
import com.dhemery.expressions.diagnosing.Diagnosis;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Indicates that a polling schedule expired before the polled condition was
 * satisfied.
 */
public class PollTimeoutException extends RuntimeException {
    public PollTimeoutException(PollingSchedule schedule, BooleanSupplier supplier) {
        super(Diagnosis.of(schedule, supplier));
    }

    public <T> PollTimeoutException(PollingSchedule schedule, T subject, Predicate<? super T> predicate) {
        super(Diagnosis.of(schedule, subject, predicate));
    }

    public <T, V> PollTimeoutException(PollingSchedule schedule, T subject, Function<? super T, V> function, Predicate<? super V> predicate, V finalFunctionValue) {
        super(Diagnosis.of(schedule, subject, function, predicate, finalFunctionValue));
    }
}
