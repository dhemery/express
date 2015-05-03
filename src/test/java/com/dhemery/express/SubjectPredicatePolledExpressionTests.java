package com.dhemery.express;

import com.dhemery.express.helpers.PolledExpressionTestSetup;
import com.dhemery.express.helpers.PollingSchedules;
import org.jmock.Expectations;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class SubjectPredicatePolledExpressionTests {
    public static final SelfDescribingPredicate<String> PREDICATE = Named.predicate("predicate", t -> true);
    public static final PollingSchedule SCHEDULE = PollingSchedules.random();

    public static class AssertThat extends PolledExpressionTestSetup {
        @Test
        public void returnsWithoutThrowing_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, "subject", PREDICATE);
                will(returnValue(true));
            }});

            expressions.assertThat(SCHEDULE, "subject", PREDICATE);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, "subject", PREDICATE);
                will(returnValue(false));
            }});

            expressions.assertThat(SCHEDULE, "subject", PREDICATE);
        }

        @Test
        public void errorMessage_describesSubjectPredicateAndSchedule() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, "subject", PREDICATE);
                will(returnValue(false));
            }});

            String message = messageThrownBy(() -> expressions.assertThat(SCHEDULE, "subject", PREDICATE));

            assertThat(message, is(Diagnosis.of(SCHEDULE, "subject", PREDICATE)));
        }
    }

    public static class SatisfiedThat extends PolledExpressionTestSetup {
        @Test
        public void returnsTrue_ifPollReturnsTrue() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, "subject", PREDICATE);
                will(returnValue(true));
            }});

            boolean result = expressions.satisfiedThat(SCHEDULE, "subject", PREDICATE);

            assertThat(result, is(true));
        }

        @Test
        public void returnsFalse_ifPollReturnsFalse() {
            context.checking(new Expectations() {{
                allowing(poller).poll(SCHEDULE, "subject", PREDICATE);
                will(returnValue(false));
            }});

            boolean result = expressions.satisfiedThat(SCHEDULE, "subject", PREDICATE);

            assertThat(result, is(false));
        }
    }}
