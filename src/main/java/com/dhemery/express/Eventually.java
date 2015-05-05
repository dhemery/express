package com.dhemery.express;

public interface Eventually {
    /**
     * Returns the default polling schedule. This factory method is named to
     * read nicely in polled expressions:
     * <pre>
     * assertThat(eventually(), jethro, is(swimmingInTheCementPond()));
     * </pre>
     *
     * @return a polling schedule with the default interval and duration
     */
    PollingSchedule eventually();
}
