package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionsPolledBy;
import com.dhemery.express.helpers.PollingSchedules;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.Duration;

import static com.dhemery.express.helpers.FunctionExpectations.appendItsStringValue;
import static com.dhemery.express.helpers.FunctionExpectations.appendTheMismatchDescriptionOfTheItem;
import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

public class PolledExpressionsWhenTests {
    private static final String SUBJECT = "subject";
    private static final String FUNCTION_VALUE = "function value";

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    Poller poller;
    @Mock
    Eventually eventually;
    @Mock
    SelfDescribingPredicate<String> predicate;

    @Mock
    SelfDescribingFunction<String, String> function;

    @Mock
    Matcher<String> matcher;

    PolledExpressions expressions;
    PollingSchedule defaultPollingSchedule;

    @Before
    public void setup() {
        expressions = new ExpressionsPolledBy(poller, eventually);

        context.checking(new Expectations() {{ //@formatter:off
            allowing(eventually).eventually();
                will(returnValue(defaultPollingSchedule));
            allowing(any(SelfDescribing.class)).method("describeTo");
                will(appendItsStringValue());
            allowing(any(Matcher.class)).method("describeMismatch");
                will(appendTheMismatchDescriptionOfTheItem());
        }}); //@formatter:on
    }

    @Test
    public void defaultScheduleWithSubjectPredicate_returnsSubject_ifPollReturnsTrue() {
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, predicate);
                will(returnValue(true));
        }}); //@formatter:on

        String returned = expressions.when(SUBJECT, predicate);
        assertThat(returned, is(sameInstance(SUBJECT)));
    }

    @Test(expected = PollTimeoutException.class)
    public void defaultScheduleWithSubjectPredicate_throwsPollTimeoutException_ifPollReturnsFalse() {
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, predicate);
                will(returnValue(false));
        }}); //@formatter:on

        expressions.when(SUBJECT, predicate);
    }

    @Test
    public void defaultScheduleWithSubjectPredicate_exceptionMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, predicate);
                will(returnValue(false));
        }}); //@formatter:on

        String message = messageThrownBy(() -> expressions.when(SUBJECT, predicate));

        assertThat(message, is(Diagnosis.of(defaultPollingSchedule, SUBJECT, predicate)));
    }

    @Test
    public void defaultScheduleWithSubjectFunctionPredicate_returnsSubject_ifPollEvaluationResultIsSatisfied() {
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, function, predicate);
                will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, true)));
        }}); //@formatter:on

        String returned = expressions.when(SUBJECT, function, predicate);
        assertThat(returned, is(sameInstance(SUBJECT)));
    }

    @Test(expected = PollTimeoutException.class)
    public void defaultScheduleWithSubjectFunctionPredicate_throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(34), Duration.ofSeconds(55));
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, function, predicate);
                will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, false)));
        }}); //@formatter:on

        expressions.when(SUBJECT, function, predicate);
    }

    @Test
    public void defaultScheduleWithSubjectFunctionPredicate_exceptionMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, function, predicate);
                will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, false)));
        }}); //@formatter:on

        String message = messageThrownBy(() -> expressions.when(SUBJECT, function, predicate));

        assertThat(message, is(Diagnosis.of(defaultPollingSchedule, SUBJECT, function, predicate, FUNCTION_VALUE)));
    }

    @Test
    public void defaultScheduleWithSubjectFunctionMatcher_returnsSubject_ifPollEvaluationResultIsSatisfied() {
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, function, matcher);
                will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, true)));
        }}); //@formatter:on

        String returned = expressions.when(SUBJECT, function, matcher);
        assertThat(returned, is(sameInstance(SUBJECT)));
    }

    @Test(expected = PollTimeoutException.class)
    public void defaultScheduleWithSubjectFunctionMatcher_throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, function, matcher);
                will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, false)));
        }}); //@formatter:on

        expressions.when(SUBJECT, function, matcher);
    }

    @Test
    public void defaultScheduleWithSubjectFunctionMatcher_exceptionMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, function, matcher);
                will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, false)));
        }}); //@formatter:on


        String message = messageThrownBy(() -> expressions.when(SUBJECT, function, matcher));

        assertThat(message, is(Diagnosis.of(defaultPollingSchedule, SUBJECT, function, matcher, FUNCTION_VALUE)));
    }

    @Test
    public void scheduleWithSubjectPredicate_returnsSubject_ifPollReturnsTrue() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, SUBJECT, predicate);
                will(returnValue(true));
        }}); //@formatter:on

        String returned = expressions.when(schedule, SUBJECT, predicate);
        assertThat(returned, is(sameInstance(SUBJECT)));
    }

    @Test(expected = PollTimeoutException.class)
    public void scheduleWithSubjectPredicate_throwsPollTimeoutException_ifPollReturnsFalse() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, SUBJECT, predicate);
                will(returnValue(false));
        }}); //@formatter:on

        expressions.when(schedule, SUBJECT, predicate);
    }

    @Test
    public void scheduleWithSubjectPredicate_exceptionMessageIncludesDiagnosis() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, SUBJECT, predicate);
                will(returnValue(false));
        }}); //@formatter:on

        String message = messageThrownBy(() -> expressions.when(schedule, SUBJECT, predicate));

        assertThat(message, is(Diagnosis.of(schedule, SUBJECT, predicate)));
    }

    @Test
    public void scheduleWithSubjectFunctionPredicate_returnsSubject_ifPollEvaluationResultIsSatisfied() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, SUBJECT, function, predicate);
                will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, true)));
        }}); //@formatter:on

        String returned = expressions.when(schedule, SUBJECT, function, predicate);
        assertThat(returned, is(sameInstance(SUBJECT)));
    }

    @Test(expected = PollTimeoutException.class)
    public void scheduleWithSubjectFunctionPredicate_throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, SUBJECT, function, predicate);
                will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, false)));
        }}); //@formatter:on

        expressions.when(schedule, SUBJECT, function, predicate);
    }

    @Test
    public void scheduleWithSubjectFunctionPredicate_exceptionMessageIncludesDiagnosis() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, SUBJECT, function, predicate);
                will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, false)));
        }}); //@formatter:on


        String message = messageThrownBy(() -> expressions.when(schedule, SUBJECT, function, predicate));

        assertThat(message, is(Diagnosis.of(schedule, SUBJECT, function, predicate, FUNCTION_VALUE)));
    }

    @Test
    public void scheduleWithSubjectFunctionMatcher_returnsSubject_ifPollEvaluationResultIsSatisfied() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, SUBJECT, function, matcher);
                will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, true)));
        }}); //@formatter:on

        String returned = expressions.when(schedule, SUBJECT, function, matcher);
        assertThat(returned, is(sameInstance(SUBJECT)));
    }

    @Test(expected = PollTimeoutException.class)
    public void scheduleWithSubjectFunctionMatcher_throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, SUBJECT, function, matcher);
                will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, false)));
        }}); //@formatter:on

        expressions.when(schedule, SUBJECT, function, matcher);
    }

    @Test
    public void scheduleWithSubjectFunctionMatcher_exceptionMessageIncludesDiagnosis() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{ //@formatter:off
            allowing(poller).poll(schedule, SUBJECT, function, matcher);
                will(returnValue(new PollEvaluationResult<>(FUNCTION_VALUE, false)));
        }}); //@formatter:on

        String message = messageThrownBy(() -> expressions.when(schedule, SUBJECT, function, matcher));

        assertThat(message, is(Diagnosis.of(schedule, SUBJECT, function, matcher, FUNCTION_VALUE)));
    }
}
