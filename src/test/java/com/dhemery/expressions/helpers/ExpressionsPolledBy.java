package com.dhemery.expressions.helpers;

import com.dhemery.expressions.PolledExpressions;
import com.dhemery.expressions.Poller;
import com.dhemery.expressions.PollingSchedule;

public class ExpressionsPolledBy implements PolledExpressions {
    private final Poller poller;
    private final PollingSchedule defaultPollingSchedule;

    public ExpressionsPolledBy(Poller poller, PollingSchedule defaultPollingSchedule) {
        this.poller = poller;
        this.defaultPollingSchedule = defaultPollingSchedule;
    }

    @Override
    public Poller poller() {
        return poller;
    }

    @Override
    public PollingSchedule eventually() {
        return defaultPollingSchedule;
    }
}
