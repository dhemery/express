package com.dhemery.express;

import com.dhemery.express.helpers.PolledExpressionTestBase;
import com.dhemery.express.helpers.PollingSchedules;
import org.jmock.auto.Mock;
import org.junit.Test;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PolledAssertThatBooleanSupplierTests extends PolledExpressionTestBase {
    @Mock
    SelfDescribingBooleanSupplier supplier;
    PollingSchedule schedule = PollingSchedules.random();

    @Test
    public void returnsWithoutThrowing_ifPollReturnsTrue() {
        givenThat(pollReturns(schedule, supplier, true));

        expressions.assertThat(schedule, supplier);
    }

    @Test(expected = AssertionError.class)
    public void throwsAssertionError_ifPollReturnsFalse() {
        givenThat(pollReturns(schedule, supplier, false));

        expressions.assertThat(schedule, supplier);
    }

    @Test
    public void errorMessageIncludesDiagnosis() {
        givenThat(pollReturns(schedule, supplier, false));

        String message = messageThrownBy(() -> expressions.assertThat(schedule, supplier));

        assertThat(message, is(Diagnosis.of(schedule, supplier)));
    }
}
