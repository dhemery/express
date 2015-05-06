package com.dhemery.expressions.helpers;

import com.dhemery.expressions.Eventually;
import com.dhemery.expressions.PolledExpressions;
import com.dhemery.expressions.Poller;
import com.dhemery.expressions.polling.PollingSchedule;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;

public class PolledExpressionTestSetup {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock public Poller poller;

    public PolledExpressions expressions;

    private PollingSchedule defaultSchedule = PollingSchedules.random();
    private Eventually eventually = () -> defaultSchedule;

    @Before
    public void polledExpressionTestSetup() {
        expressions = new ExpressionsPolledBy(poller, eventually);
    }
}
