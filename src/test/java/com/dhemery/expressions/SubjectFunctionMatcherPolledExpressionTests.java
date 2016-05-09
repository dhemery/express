package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import com.dhemery.expressions.helpers.PolledExpressionTestSetup;
import com.dhemery.expressions.helpers.PollingSchedules;
import com.dhemery.expressions.polling.PollEvaluationResult;
import com.dhemery.expressions.polling.PollTimeoutException;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static com.dhemery.expressions.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SubjectFunctionMatcherPolledExpressionTests {
    public static final PollingSchedule SCHEDULE = PollingSchedules.random();
    public static final String SUBJECT = "subject";
    public static final Function<String, String> FUNCTION = Named.function("function", String::toUpperCase);
    public static final Matcher<Object> MATCHER = anything();

    public static class AssertThat extends PolledExpressionTestSetup {
        @Test
        public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            expressions.assertThat(SCHEDULE, SUBJECT, FUNCTION, MATCHER);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            expressions.assertThat(SCHEDULE, SUBJECT, FUNCTION, MATCHER);
        }

        @Test
        public void errorMessage_describesSubjectFunctionMatcherMismatchAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            String message = messageThrownBy(() -> expressions.assertThat(SCHEDULE, SUBJECT, FUNCTION, MATCHER));

            assertThat(message, is(Diagnosis.of(SCHEDULE, SUBJECT, FUNCTION, MATCHER, FUNCTION.apply(SUBJECT))));
        }
    }

    public static class SatisfiedThat extends PolledExpressionTestSetup {
        @Test
        public void returnsTrue_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            boolean result = expressions.satisfiedThat(SCHEDULE, SUBJECT, FUNCTION, MATCHER);

            assertThat(result, is(true));
        }

        @Test
        public void returnsFalse_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            boolean result = expressions.satisfiedThat(SCHEDULE, SUBJECT, FUNCTION, MATCHER);

            assertThat(result, is(false));
        }
    }

    public static class WaitUntilWithDefaultPollingSchedule extends PolledExpressionTestSetup {
        PollingSchedule defaultSchedule;

        @Before
        public void setup() {
            defaultSchedule = expressions.eventually();
        }

        @Test
        public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            expressions.waitUntil(SUBJECT, FUNCTION, MATCHER);
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            expressions.waitUntil(SUBJECT, FUNCTION, MATCHER);
        }

        @Test
        public void exceptionMessage_describesSubjectFunctionMatcherMismatchAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            String message = messageThrownBy(() -> expressions.waitUntil(SUBJECT, FUNCTION, MATCHER));

            assertThat(message, is(Diagnosis.of(defaultSchedule, SUBJECT, FUNCTION, MATCHER, FUNCTION.apply(SUBJECT))));
        }
    }

    public static class WaitUntilWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            expressions.waitUntil(schedule, SUBJECT, FUNCTION, MATCHER);
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            expressions.waitUntil(schedule, SUBJECT, FUNCTION, MATCHER);
        }

        @Test
        public void exceptionMessage_describesSubjectFunctionMatcherMismatchAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            String message = messageThrownBy(() -> expressions.waitUntil(schedule, SUBJECT, FUNCTION, MATCHER));

            assertThat(message, is(Diagnosis.of(schedule, SUBJECT, FUNCTION, MATCHER, FUNCTION.apply(SUBJECT))));
        }
    }

    public static class WhenWithDefaultPollingSchedule extends PolledExpressionTestSetup {
        public static final String SUBJECT = SubjectFunctionMatcherPolledExpressionTests.SUBJECT;
        PollingSchedule defaultSchedule;

        @Before
        public void setup() {
            defaultSchedule = expressions.eventually();
        }

        @Test
        public void returnsSubject_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SubjectFunctionMatcherPolledExpressionTests.SUBJECT), true)));
            }});

            String returnedValue = expressions.when(SUBJECT, FUNCTION, MATCHER);
            assertThat(returnedValue, is(sameInstance(SUBJECT)));
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SubjectFunctionMatcherPolledExpressionTests.SUBJECT), false)));
            }});

            expressions.when(SUBJECT, FUNCTION, MATCHER);
        }

        @Test
        public void exceptionMessage_describesSubjectFunctionMatcherMismatchAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SubjectFunctionMatcherPolledExpressionTests.SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SubjectFunctionMatcherPolledExpressionTests.SUBJECT), false)));
            }});

            String message = messageThrownBy(() -> expressions.when(SubjectFunctionMatcherPolledExpressionTests.SUBJECT, FUNCTION, MATCHER));

            assertThat(message, is(Diagnosis.of(defaultSchedule, SubjectFunctionMatcherPolledExpressionTests.SUBJECT, FUNCTION, MATCHER, FUNCTION.apply(SubjectFunctionMatcherPolledExpressionTests.SUBJECT))));
        }
    }

    public static class WhenWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsSubject_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), true)));
            }});

            String returnedValue = expressions.when(schedule, SUBJECT, FUNCTION, MATCHER);
            assertThat(returnedValue, is(sameInstance(SUBJECT)));
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            expressions.when(schedule, SUBJECT, FUNCTION, MATCHER);
        }

        @Test
        public void exceptionMessage_describesSubjectFunctionMatcherMismatchAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply(SUBJECT), false)));
            }});

            String message = messageThrownBy(() -> expressions.when(schedule, SUBJECT, FUNCTION, MATCHER));

            assertThat(message, is(Diagnosis.of(schedule, SUBJECT, FUNCTION, MATCHER, FUNCTION.apply(SUBJECT))));
        }
    }
}
