package com.dhemery.express;

import java.util.function.BooleanSupplier;

/**
 * Indicates that a poll timed out before the polled condition was satisfied.
 */
public class PollTimeoutException extends RuntimeException {
    public PollTimeoutException(BooleanSupplier condition, PollingSchedule schedule) {
        super(Diagnosis.of(condition, schedule));
    }
}
