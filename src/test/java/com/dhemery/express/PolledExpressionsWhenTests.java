package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionsPolledBy;
import com.dhemery.express.helpers.PollingSchedules;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

// TODO: Distribute to parameter-based test classes
public class PolledExpressionsWhenTests {
    private static final String SUBJECT = "subject";
    private static final SelfDescribingFunction<String, String> FUNCTION = Named.function("function", String::toUpperCase);
    private static final SelfDescribingPredicate<String> PREDICATE = Named.predicate("predicate", t -> true);
    private static final Matcher<Object> MATCHER = anything();

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock Poller poller;
    @Mock Eventually eventually;

    PolledExpressions expressions;
    PollingSchedule defaultPollingSchedule;

    @Before
    public void setup() {
        expressions = new ExpressionsPolledBy(poller, eventually);

        context.checking(new Expectations() {{
            allowing(eventually).eventually();
            will(returnValue(defaultPollingSchedule));
        }});
    }

    @Test
    public void defaultScheduleWithSubjectPredicate_returnsSubject_ifPollReturnsTrue() {
        context.checking(new Expectations() {{
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, PREDICATE);
            will(returnValue(true));
        }});

        String returned = expressions.when(SUBJECT, PREDICATE);

        assertThat(returned, is(sameInstance(SUBJECT)));
    }

    @Test(expected = PollTimeoutException.class)
    public void defaultScheduleWithSubjectPredicate_throwsPollTimeoutException_ifPollReturnsFalse() {
        context.checking(new Expectations() {{
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, PREDICATE);
            will(returnValue(false));
        }});

        expressions.when(SUBJECT, PREDICATE);
    }

    @Test
    public void defaultScheduleWithSubjectPredicate_exceptionMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, PREDICATE);
            will(returnValue(false));
        }});

        String message = messageThrownBy(() -> expressions.when(SUBJECT, PREDICATE));

        assertThat(message, is(Diagnosis.of(defaultPollingSchedule, SUBJECT, PREDICATE)));
    }

    @Test
    public void defaultScheduleWithSubjectFunctionPredicate_returnsSubject_ifPollEvaluationResultIsSatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, FUNCTION, PREDICATE);
            will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
        }});

        String returned = expressions.when(SUBJECT, FUNCTION, PREDICATE);
        assertThat(returned, is(sameInstance(SUBJECT)));
    }

    @Test(expected = PollTimeoutException.class)
    public void defaultScheduleWithSubjectFunctionPredicate_throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, FUNCTION, PREDICATE);
            will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
        }});

        expressions.when(SUBJECT, FUNCTION, PREDICATE);
    }

    @Test
    public void defaultScheduleWithSubjectFunctionPredicate_exceptionMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, FUNCTION, PREDICATE);
            will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
        }});

        String message = messageThrownBy(() -> expressions.when(SUBJECT, FUNCTION, PREDICATE));

        assertThat(message, is(Diagnosis.of(defaultPollingSchedule, SUBJECT, FUNCTION, PREDICATE, FUNCTION.apply(SUBJECT))));
    }

    @Test
    public void defaultScheduleWithSubjectFunctionMatcher_returnsSubject_ifPollEvaluationResultIsSatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, FUNCTION, MATCHER);
            will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
        }});

        String returned = expressions.when(SUBJECT, FUNCTION, MATCHER);
        assertThat(returned, is(sameInstance(SUBJECT)));
    }

    @Test(expected = PollTimeoutException.class)
    public void defaultScheduleWithSubjectFunctionMatcher_throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
        context.checking(new Expectations() {{
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, FUNCTION, MATCHER);
            will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
        }});

        expressions.when(SUBJECT, FUNCTION, MATCHER);
    }

    @Test
    public void defaultScheduleWithSubjectFunctionMatcher_exceptionMessageIncludesDiagnosis() {
        context.checking(new Expectations() {{
            allowing(poller).poll(defaultPollingSchedule, SUBJECT, FUNCTION, MATCHER);
            will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
        }});


        String message = messageThrownBy(() -> expressions.when(SUBJECT, FUNCTION, MATCHER));

        assertThat(message, is(Diagnosis.of(defaultPollingSchedule, SUBJECT, FUNCTION, MATCHER, FUNCTION.apply(SUBJECT))));
    }

    @Test
    public void scheduleWithSubjectPredicate_returnsSubject_ifPollReturnsTrue() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, PREDICATE);
            will(returnValue(true));
        }});

        String returned = expressions.when(schedule, SUBJECT, PREDICATE);
        assertThat(returned, is(sameInstance(SUBJECT)));
    }

    @Test(expected = PollTimeoutException.class)
    public void scheduleWithSubjectPredicate_throwsPollTimeoutException_ifPollReturnsFalse() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, PREDICATE);
            will(returnValue(false));
        }});

        expressions.when(schedule, SUBJECT, PREDICATE);
    }

    @Test
    public void scheduleWithSubjectPredicate_exceptionMessageIncludesDiagnosis() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, PREDICATE);
            will(returnValue(false));
        }});

        String message = messageThrownBy(() -> expressions.when(schedule, SUBJECT, PREDICATE));

        assertThat(message, is(Diagnosis.of(schedule, SUBJECT, PREDICATE)));
    }

    @Test
    public void scheduleWithSubjectFunctionPredicate_returnsSubject_ifPollEvaluationResultIsSatisfied() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
            will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
        }});

        String returned = expressions.when(schedule, SUBJECT, FUNCTION, PREDICATE);
        assertThat(returned, is(sameInstance(SUBJECT)));
    }

    @Test(expected = PollTimeoutException.class)
    public void scheduleWithSubjectFunctionPredicate_throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
            will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
        }});

        expressions.when(schedule, SUBJECT, FUNCTION, PREDICATE);
    }

    @Test
    public void scheduleWithSubjectFunctionPredicate_exceptionMessageIncludesDiagnosis() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, FUNCTION, PREDICATE);
            will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
        }});


        String message = messageThrownBy(() -> expressions.when(schedule, SUBJECT, FUNCTION, PREDICATE));

        assertThat(message, is(Diagnosis.of(schedule, SUBJECT, FUNCTION, PREDICATE, FUNCTION.apply(SUBJECT))));
    }

    @Test
    public void scheduleWithSubjectFunctionMatcher_returnsSubject_ifPollEvaluationResultIsSatisfied() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
            will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
        }});

        String returned = expressions.when(schedule, SUBJECT, FUNCTION, MATCHER);
        assertThat(returned, is(sameInstance(SUBJECT)));
    }

    @Test(expected = PollTimeoutException.class)
    public void scheduleWithSubjectFunctionMatcher_throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
            will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
        }});

        expressions.when(schedule, SUBJECT, FUNCTION, MATCHER);
    }

    @Test
    public void scheduleWithSubjectFunctionMatcher_exceptionMessageIncludesDiagnosis() {
        PollingSchedule schedule = PollingSchedules.random();
        context.checking(new Expectations() {{
            allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
            will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
        }});

        String message = messageThrownBy(() -> expressions.when(schedule, SUBJECT, FUNCTION, MATCHER));

        assertThat(message, is(Diagnosis.of(schedule, SUBJECT, FUNCTION, MATCHER, FUNCTION.apply(SUBJECT))));
    }
}
