package com.dhemery.expressions.polling;

import java.time.Duration;

/**
 * Pauses execution for a specified duration.
 */
public interface Sleeper {
    /**
     * Pauses execution for the given duration.
     *
     * @param sleepDuration
     *         how long to pause execution
     */
    void sleep(Duration sleepDuration);
}
