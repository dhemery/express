package com.dhemery.express;

import com.dhemery.express.helpers.PolledExpressionTestSetup;
import com.dhemery.express.helpers.PollingSchedules;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class SubjectFunctionMatcherPolledExpressionTests {
    public static final PollingSchedule SCHEDULE = PollingSchedules.random();
    public static final SelfDescribingFunction<String, String> FUNCTION = Named.function("function", String::toUpperCase);
    public static final Matcher<Object> MATCHER = anything();

    public static class AssertThat extends PolledExpressionTestSetup {
        @Test
        public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, "subject", FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), true)));
            }});

            expressions.assertThat(SCHEDULE, "subject", FUNCTION, MATCHER);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, "subject", FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            expressions.assertThat(SCHEDULE, "subject", FUNCTION, MATCHER);
        }

        @Test
        public void errorMessage_describesSubjectFunctionMatcherMismatchAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, "subject", FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            String message = messageThrownBy(() -> expressions.assertThat(SCHEDULE, "subject", FUNCTION, MATCHER));

            assertThat(message, is(Diagnosis.of(SCHEDULE, "subject", FUNCTION, MATCHER, FUNCTION.apply("subject"))));
        }
    }

    public static class SatisfiedThat extends PolledExpressionTestSetup {
        @Test
        public void returnsTrue_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, "subject", FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), true)));
            }});

            boolean result = expressions.satisfiedThat(SCHEDULE, "subject", FUNCTION, MATCHER);

            assertThat(result, is(true));
        }

        @Test
        public void returnsFalse_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, "subject", FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            boolean result = expressions.satisfiedThat(SCHEDULE, "subject", FUNCTION, MATCHER);

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
                allowing(poller).poll(defaultSchedule, "subject", FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), true)));
            }});

            expressions.waitUntil("subject", FUNCTION, MATCHER);
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, "subject", FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            expressions.waitUntil("subject", FUNCTION, MATCHER);
        }

        @Test
        public void exceptionMessage_describesSubjectFunctionMatcherMismatchAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, "subject", FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            String message = messageThrownBy(() -> expressions.waitUntil("subject", FUNCTION, MATCHER));

            assertThat(message, is(Diagnosis.of(defaultSchedule, "subject", FUNCTION, MATCHER, FUNCTION.apply("subject"))));
        }
    }
    public static class WaitUntilWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, "subject", FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), true)));
            }});

            expressions.waitUntil(schedule, "subject", FUNCTION, MATCHER);
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, "subject", FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            expressions.waitUntil(schedule, "subject", FUNCTION, MATCHER);
        }

        @Test
        public void exceptionMessage_describesSubjectFunctionMatcherMismatchAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, "subject", FUNCTION, MATCHER);
                will(returnValue(new PollEvaluationResult<>(FUNCTION.apply("subject"), false)));
            }});

            String message = messageThrownBy(() -> expressions.waitUntil(schedule, "subject", FUNCTION, MATCHER));

            assertThat(message, is(Diagnosis.of(schedule, "subject", FUNCTION, MATCHER, FUNCTION.apply("subject"))));
        }
    }
}
