package com.dhemery.expressions;

/**
 * A factory method for default polling schedules.
 */
public interface Eventually {
    /**
     * Returns a polling schedule with the default polling interval and
     * duration.
     * <p>
     * This factory method is named to read like a time frame in polled expressions:
     *
     * <pre>
     * assertThat(<strong>eventually()</strong>, searchField, isDisplayed());
     * </pre>
     *
     * The default polling interval and duration are defined by each
     * implementation.
     * Implementors must decide what polling schedule is a reasonable default for
     * their purposes.
     * <p>
     * There is no requirement that a new instance be returned each time this
     * method is invoked. If the implementation returns different instances, all
     * instances must be pairwise equal.
     *
     * @return a polling schedule with the default polling interval and duration
     *
     * @see PollingSchedule#equals
     */
    PollingSchedule eventually();
}
