package com.dhemery.express;

/**
 * Indicates that a poll timed out before the polled condition was satisfied.
 */
public class PollTimeoutException extends RuntimeException {
    public PollTimeoutException(Condition condition, PollingSchedule schedule) {
        super(Diagnosis.of(condition, schedule));
    }
}
