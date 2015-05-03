package com.dhemery.express.helpers;

import com.dhemery.express.PolledExpressions;
import com.dhemery.express.Poller;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;

public class PolledExpressionTestSetup {

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock public Poller poller;

    public PolledExpressions expressions;

    @Before
    public void polledExpressionTestSetup() {
        expressions = new ExpressionsPolledBy(poller);
    }
}
