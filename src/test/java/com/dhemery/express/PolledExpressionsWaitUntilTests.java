package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionsPolledBy;
import com.dhemery.express.helpers.PollingSchedules;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.Duration;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;

// TODO: Distribute to parameter-based test classes
public class PolledExpressionsWaitUntilTests {
    private static final SelfDescribingBooleanSupplier SUPPLIER = Named.booleanSupplier("boolean supplier", () -> false);
    private static final SelfDescribingFunction<String, String> FUNCTION = Named.function("function", Function.identity());
    private static final Matcher<String> MATCHER = isEmptyString();
    private static final SelfDescribingPredicate<String> PREDICATE = Named.predicate("predicate", t -> true);

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock Poller poller;

    PolledExpressions expressions;
    PollingSchedule schedule = PollingSchedules.random();

    @Before
    public void setup() {
        expressions = new ExpressionsPolledBy(poller);
    }



    @Test
    public void defaultScheduleWithSubjectFunctionMatcher_returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(89), Duration.ofSeconds(144));
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
                return new PollEvaluationResult<>(null, true);
            }

            @Override
            public PollingSchedule eventually() {
                return defaultSchedule;
            }
        };
        expressions.waitUntil("subject", FUNCTION, MATCHER);
    }

    @Test(expected = PollTimeoutException.class)
    public void defaultScheduleWithSubjectFunctionMatcher_throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(144), Duration.ofSeconds(233));
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
                return new PollEvaluationResult<>(null, false);
            }

            @Override
            public PollingSchedule eventually() {
                return defaultSchedule;
            }
        };
        expressions.waitUntil("subject", FUNCTION, MATCHER);
    }

    @Test
    public void defaultScheduleWithSubjectFunctionMatcher_exceptionMessageIncludesDiagnosis() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(233), Duration.ofSeconds(337));
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
                return new PollEvaluationResult<>(function.apply(subject), false);
            }

            @Override
            public PollingSchedule eventually() {
                return defaultSchedule;
            }
        };

        String message = messageThrownBy(() -> expressions.waitUntil("subject", FUNCTION, MATCHER));

        assertThat(message, is(Diagnosis.of(defaultSchedule, "subject", FUNCTION, MATCHER, FUNCTION.apply("subject"))));
    }

    @Test
    public void scheduleWithSubjectFunctionMatcher_returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
                return new PollEvaluationResult<>(null, true);
            }
        };
        expressions.waitUntil(schedule, "subject", FUNCTION, MATCHER);
    }

    @Test(expected = PollTimeoutException.class)
    public void scheduleWithSubjectFunctionMatcher_throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
                return new PollEvaluationResult<>(null, false);
            }
        };
        expressions.waitUntil(schedule, "subject", FUNCTION, MATCHER);
    }

    @Test
    public void scheduleWithSubjectFunctionMatcher_exceptionMessageIncludesDiagnosis() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
                return new PollEvaluationResult<>(function.apply(subject), false);
            }
        };

        String message = messageThrownBy(() -> expressions.waitUntil(schedule, "subject", FUNCTION, MATCHER));

        assertThat(message, is(Diagnosis.of(schedule, "subject", FUNCTION, MATCHER, FUNCTION.apply("subject"))));
    }
}
