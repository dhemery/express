package com.dhemery.express;

/**
 * Poll a condition until it is satisfied.
 */
public interface Poller {
    /**
     * Poll the condition on the given polling schedule.
     * @return {@code true} if the condition is satisfied before the polling schedule expires,
     * otherwise {@code false}.
     */
    boolean poll(Condition condition, PollingSchedule schedule);

    /**
     * Return the default polling schedule.
     * @return the default polling schedule
     */
    PollingSchedule eventually();
}
