package com.dhemery.expressions.helpers;

import com.dhemery.expressions.PolledExpressions;
import com.dhemery.expressions.Poller;
import com.dhemery.expressions.PollingSchedule;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.jupiter.api.BeforeEach;

public class PolledExpressionTestSetup {
    protected Mockery context = new JUnit4Mockery();
    public Poller poller;

    public PolledExpressions expressions;

    private PollingSchedule defaultSchedule = PollingSchedules.random();

    @BeforeEach
    public void polledExpressionTestSetup() {
        poller = context.mock(Poller.class);
        expressions = new ExpressionsPolledBy(poller, defaultSchedule);
    }
}
