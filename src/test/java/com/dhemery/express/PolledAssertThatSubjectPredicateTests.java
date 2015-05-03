package com.dhemery.express;

import com.dhemery.express.helpers.PolledExpressionTestBase;
import com.dhemery.express.helpers.PollingSchedules;
import org.jmock.auto.Mock;
import org.junit.Test;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PolledAssertThatSubjectPredicateTests extends PolledExpressionTestBase {
    private static final String subject = "subject";
    private final PollingSchedule schedule = PollingSchedules.random();

    @Mock
    SelfDescribingPredicate<String> predicate;

    @Test
    public void returnsWithoutThrowing_ifPollReturnsTrue() {
        givenThat(pollReturns(schedule, subject, predicate, true));

        expressions.assertThat(schedule, subject, predicate);
    }

    @Test(expected = AssertionError.class)
    public void throwsAssertionError_ifPollReturnsFalse() {
        givenThat(pollReturns(schedule, subject, predicate, false));

        expressions.assertThat(schedule, subject, predicate);
    }

    @Test
    public void errorMessageIncludesDiagnosis() {
        givenThat(pollReturns(schedule, subject, predicate, false));

        String message = messageThrownBy(() -> expressions.assertThat(schedule, subject, predicate));

        assertThat(message, is(Diagnosis.of(schedule, subject, predicate)));
    }
}
