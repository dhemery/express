package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.helpers.PolledExpressionTestSetup;
import com.dhemery.expressions.helpers.PollingSchedules;
import com.dhemery.expressions.polling.PollTimeoutException;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import static com.dhemery.expressions.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

public class SubjectPredicatePolledExpressionTests {
    public static final String SUBJECT = "subject";
    public static final SelfDescribingPredicate<String> PREDICATE = Named.predicate("predicate", t -> true);

    public static class AssertThat extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(true));
            }});

            expressions.assertThat(schedule, SUBJECT, PREDICATE);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            expressions.assertThat(schedule, SUBJECT, PREDICATE);
        }

        @Test
        public void errorMessage_describesSubjectPredicateAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            String message = messageThrownBy(() -> expressions.assertThat(schedule, SUBJECT, PREDICATE));

            assertThat(message, is(Diagnosis.of(schedule, SUBJECT, PREDICATE)));
        }
    }

    public static class SatisfiedThat extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsTrue_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(true));
            }});

            boolean result = expressions.satisfiedThat(schedule, SUBJECT, PREDICATE);

            assertThat(result, is(true));
        }

        @Test
        public void returnsFalse_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            boolean result = expressions.satisfiedThat(schedule, SUBJECT, PREDICATE);

            assertThat(result, is(false));
        }
    }

    public static class WaitUntilWithDefaultPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule defaultSchedule;

        @Before
        public void setUp() {
            defaultSchedule = expressions.eventually();
        }

        @Test
        public void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, PREDICATE);
                will(returnValue(true));
            }});

            expressions.waitUntil(SUBJECT, PREDICATE);
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            expressions.waitUntil(SUBJECT, PREDICATE);
        }

        @Test
        public void exceptionMessage_describesSubjectPredicateAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(expressions.eventually(), SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            String message = messageThrownBy(() -> expressions.waitUntil(SUBJECT, PREDICATE));

            assertThat(message, is(Diagnosis.of(expressions.eventually(), SUBJECT, PREDICATE)));
        }
    }

    public static class WaitUntilWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(true));
            }});

            expressions.waitUntil(schedule, SUBJECT, PREDICATE);
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            expressions.waitUntil(schedule, SUBJECT, PREDICATE);
        }

        @Test
        public void exceptionMessage_describesSubjectPredicateAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            String message = messageThrownBy(() -> expressions.waitUntil(schedule, SUBJECT, PREDICATE));

            assertThat(message, is(Diagnosis.of(schedule, SUBJECT, PREDICATE)));
        }
    }

    public static class WhenWithDefaultPollingSchedule extends PolledExpressionTestSetup {
        private PollingSchedule defaultSchedule;

        @Before
        public void setUp() {
            defaultSchedule = expressions.eventually();
        }

        @Test
        public void returnsSubject_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, PREDICATE);
                will(returnValue(true));
            }});

            String returnedValue = expressions.when(SUBJECT, PREDICATE);
            assertThat(returnedValue, is(sameInstance(SUBJECT)));
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(defaultSchedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            expressions.when(SUBJECT, PREDICATE);
        }

        @Test
        public void exceptionMessage_describesSubjectPredicateAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(expressions.eventually(), SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            String message = messageThrownBy(() -> expressions.when(SUBJECT, PREDICATE));

            assertThat(message, is(Diagnosis.of(expressions.eventually(), SUBJECT, PREDICATE)));
        }
    }

    public static class WhenWithExplicitPollingSchedule extends PolledExpressionTestSetup {
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsSubject_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(true));
            }});

            String returnedValue = expressions.when(schedule, SUBJECT, PREDICATE);
            assertThat(returnedValue, is(sameInstance(SUBJECT)));
        }

        @Test(expected = PollTimeoutException.class)
        public void throwsPollTimeoutException_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            expressions.when(schedule, SUBJECT, PREDICATE);
        }

        @Test
        public void exceptionMessage_describesSubjectPredicateAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(schedule, SUBJECT, PREDICATE);
                will(returnValue(false));
            }});

            String message = messageThrownBy(() -> expressions.when(schedule, SUBJECT, PREDICATE));

            assertThat(message, is(Diagnosis.of(schedule, SUBJECT, PREDICATE)));
        }
    }
}
