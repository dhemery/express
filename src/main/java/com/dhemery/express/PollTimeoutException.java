package com.dhemery.express;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;

/**
 * Indicates that a polling schedule expired before the polled condition was
 * satisfied.
 */
public class PollTimeoutException extends RuntimeException {
    public PollTimeoutException(PollingSchedule schedule, SelfDescribing condition) {
        super(Diagnosis.of(schedule, condition));
    }

    public PollTimeoutException(PollingSchedule schedule, Object subject, SelfDescribing predicate) {
        super(Diagnosis.of(schedule, subject, predicate));
    }

    public PollTimeoutException(PollingSchedule schedule, Object subject, SelfDescribing function, SelfDescribing predicate, Object finalFunctionValue) {
        super(Diagnosis.of(schedule, subject, function, predicate, finalFunctionValue));
    }

    public PollTimeoutException(PollingSchedule schedule, Object subject, SelfDescribing function, Matcher<?> matcher, Object finalFunctionValue) {
        super(Diagnosis.of(schedule, subject, function, matcher, finalFunctionValue));
    }
}
