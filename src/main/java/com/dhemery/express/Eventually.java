package com.dhemery.express;

public interface Eventually {
    /**
     * Returns the default polling schedule.
     * <p>
     * This factory method is named to read nicely in polled expressions:
     * <p>
     * <pre>
     * assertThat(eventually(), jethro, is(swimmingInTheCementPond()));
     * </pre>
     *
     * @return a polling schedule with the default interval and duration
     */
    PollingSchedule eventually();
}
