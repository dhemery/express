package com.dhemery.express;

/**
 * A factory method to supply polling schedules with the default polling
 * interval and duration.
 */
public interface Eventually {
    /**
     * Returns a polling schedule with the default polling interval and
     * duration.
     *
     * <p>The default polling interval and duration are defined by each
     * implementation.
     *
     * <p>The polling schedules returned by this method must all
     * have the same polling interval and the same polling duration.
     * There is no requirement that each call return a new instance.
     *
     * <p>This factory method is named to read nicely in polled expressions:
     *
     * <pre>
     * assertThat(eventually(), searchField, isDisplayed());
     * </pre>
     * @implSpec
     * @return a polling schedule with the default polling interval and duration
     */
    PollingSchedule eventually();
}
