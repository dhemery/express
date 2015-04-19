package com.dhemery.express;

import java.util.function.BooleanSupplier;

/**
 * Poll a condition until it is satisfied.
 */
public interface Poller {
    /**
     * Poll the condition on the given polling schedule.
     * @return {@code true} if the condition is satisfied before the polling schedule expires,
     * otherwise {@code false}.
     */
    boolean poll(BooleanSupplier condition, PollingSchedule schedule);

    /**
     * Return the default polling schedule.
     * This is a factory method, named to read nicely in {@link PolledExpressions polled expressions}.
     * @return the default polling schedule
     */
    PollingSchedule eventually();
}
