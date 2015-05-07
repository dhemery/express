package com.dhemery.expressions.polling;

import com.dhemery.expressions.PollingSchedule;
import com.dhemery.expressions.SelfDescribingBooleanSupplier;
import com.dhemery.expressions.SelfDescribingFunction;
import com.dhemery.expressions.SelfDescribingPredicate;
import com.dhemery.expressions.diagnosing.Diagnosis;
import org.hamcrest.Matcher;

/**
 * Indicates that a polling schedule expired before the polled condition was
 * satisfied.
 */
public class PollTimeoutException extends RuntimeException {
    public PollTimeoutException(PollingSchedule schedule, SelfDescribingBooleanSupplier supplier) {
        super(Diagnosis.of(schedule, supplier));
    }

    public <T> PollTimeoutException(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        super(Diagnosis.of(schedule, subject, predicate));
    }

    public <T, V> PollTimeoutException(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate, V finalFunctionValue) {
        super(Diagnosis.of(schedule, subject, function, predicate, finalFunctionValue));
    }

    public <T, V> PollTimeoutException(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher, V finalFunctionValue) {
        super(Diagnosis.of(schedule, subject, function, matcher, finalFunctionValue));
    }
}
