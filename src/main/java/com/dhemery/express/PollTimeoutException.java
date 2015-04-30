package com.dhemery.express;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * Indicates that a polling schedule expired before the polled condition was
 * satisfied.
 */
public class PollTimeoutException extends RuntimeException {
    public PollTimeoutException() {
    }

    public PollTimeoutException(BooleanSupplier condition) {
        super(condition.toString());
    }

    public PollTimeoutException(PollingSchedule schedule, SelfDescribing condition) {
        super(Diagnosis.of(schedule, condition));
    }

    public PollTimeoutException(PollingSchedule schedule, Object subject, SelfDescribing predicate) {
        super(Diagnosis.of(schedule, subject, predicate));
    }

    public PollTimeoutException(PollingSchedule schedule, Object subject, SelfDescribing function, SelfDescribing predicate, Object value) {
        super(Diagnosis.of(schedule, subject, function, predicate, value));
    }

    public PollTimeoutException(PollingSchedule schedule, Object subject, SelfDescribing function, Matcher<?> matcher, Object value) {
        super(Diagnosis.of(schedule, subject, function, matcher, value));
    }
}
