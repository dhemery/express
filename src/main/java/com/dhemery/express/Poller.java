package com.dhemery.express;

import java.util.function.BooleanSupplier;

/**
 * Poll a condition until it is satisfied.
 */
public interface Poller {
    /**
     * Poll the condition.
     * @param condition the condition to poll
     * @param schedule the schedule on which to poll the condition
     * @return {@code true} if the condition is satisfied before the polling schedule expires,
     * otherwise {@code false}.
     */
    boolean poll(BooleanSupplier condition, PollingSchedule schedule);

    /**
     * Return the default polling schedule.
     * This factory method is named to read nicely in {@link PolledExpressions polled expressions}.
     * @return the default polling schedule
     */
    PollingSchedule eventually();
}
