package com.dhemery.express;

import java.util.function.BooleanSupplier;

/**
 * Indicates that a polling schedule expired before the polled condition was
 * satisfied.
 */
public class PollTimeoutException extends RuntimeException {
    // TODO: Remove this
    public PollTimeoutException() {}
    public PollTimeoutException(PollingSchedule schedule, BooleanSupplier condition) {
        super(schedule.toString() + condition);
    }

    public PollTimeoutException(BooleanSupplier condition) {
        super(condition.toString());
    }
}
