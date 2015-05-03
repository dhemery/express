package com.dhemery.express.helpers;

import com.dhemery.express.*;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Before;

public class PolledExpressionTestBase extends ExpressionTestBase {
    @Mock
    public Poller poller;

    public PolledExpressions expressions;


    @Before
    public void polledExpressionTestBaseSetup() {
        expressions = new ExpressionsPolledBy(poller);
    }


    protected Expectations pollReturns(PollingSchedule schedule, SelfDescribingBooleanSupplier supplier, boolean returnValue) {
        return new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, supplier);
                will(returnValue(returnValue));
        }}; //@formatter:on
    }

    protected Expectations pollReturns(PollingSchedule schedule, String subject, SelfDescribingPredicate<String> predicate, boolean returnValue) {
        return new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, subject, predicate);
                will(returnValue(returnValue));
        }}; //@formatter:on
    }

    protected Expectations pollReturns(PollingSchedule schedule, String subject, SelfDescribingFunction<String, String> function, SelfDescribingPredicate<String> predicate, PollEvaluationResult<String> result) {
        return new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, subject, function, predicate);
                will(returnValue(result));
        }}; //@formatter:on
    }

    protected Expectations pollReturns(PollingSchedule schedule, String subject, SelfDescribingFunction<String, String> function, Matcher<String> matcher, PollEvaluationResult<String> result) {
        return new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, subject, function, matcher);
                will(returnValue(result));
        }}; //@formatter:on
    }
}
