package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionsPolledBy;
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

public class PolledExpressionsAssertThatTests {
    private static final SelfDescribingBooleanSupplier A_BOOLEAN_SUPPLIER = Named.booleanSupplier("boolean supplier", () -> false);
    private static final SelfDescribingFunction<String, String> A_FUNCTION = Named.function("function", Function.identity());
    private static final Matcher<String> A_MATCHER = isEmptyString();
    private static final PollingSchedule A_POLLING_SCHEDULE = new PollingSchedule(Duration.ofMillis(1000), Duration.ofSeconds(60));
    private static final SelfDescribingPredicate<String> A_PREDICATE = Named.predicate("predicate", t -> true);
    private static final String A_SUBJECT = "";

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    Poller poller;

    PolledExpressions expressions;

    @Before
    public void setup() {
        expressions = new ExpressionsPolledBy(poller);
    }

    @Test
    public void withBooleanSupplier_returnsWithoutThrowing_ifPollReturnsTrue() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <C extends SelfDescribing & BooleanSupplier>
            boolean poll(PollingSchedule schedule, C supplier) {
                return true;
            }
        };
        expressions.assertThat(A_POLLING_SCHEDULE, A_BOOLEAN_SUPPLIER);
    }

    @Test(expected = AssertionError.class)
    public void withBooleanSupplier_throwsAssertionError_ifPollReturnsFalse() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <C extends SelfDescribing & BooleanSupplier>
            boolean poll(PollingSchedule schedule, C supplier) {
                return false;
            }
        };
        expressions.assertThat(A_POLLING_SCHEDULE, A_BOOLEAN_SUPPLIER);
    }

    @Test
    public void withBooleanSupplier_errorMessageIncludesDiagnosis() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <C extends SelfDescribing & BooleanSupplier>
            boolean poll(PollingSchedule schedule, C supplier) {
                return false;
            }
        };
        PollingSchedule schedule = A_POLLING_SCHEDULE;
        SelfDescribingBooleanSupplier supplier = A_BOOLEAN_SUPPLIER;

        String message = messageThrownBy(() -> expressions.assertThat(schedule, supplier));

        assertThat(message, is(Diagnosis.of(schedule, supplier)));
    }

    @Test
    public void withSubjectPredicate_returnsWithoutThrowing_ifPollReturnsTrue() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, P extends SelfDescribing & Predicate<? super T>>
            boolean poll(PollingSchedule schedule, T subject, P predicate) {
                return true;
            }
        };
        expressions.assertThat(A_POLLING_SCHEDULE, A_SUBJECT, A_PREDICATE);
    }

    @Test(expected = AssertionError.class)
    public void withSubjectPredicate_throwsAssertionError_ifPollReturnsFalse() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, P extends SelfDescribing & Predicate<? super T>>
            boolean poll(PollingSchedule schedule, T subject, P predicate) {
                return false;
            }
        };
        expressions.assertThat(A_POLLING_SCHEDULE, A_SUBJECT, A_PREDICATE);
    }

    @Test
    public void withSubjectPredicate_errorMessageIncludesDiagnosis() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, P extends SelfDescribing & Predicate<? super T>>
            boolean poll(PollingSchedule schedule, T subject, P predicate) {
                return false;
            }
        };
        PollingSchedule schedule = A_POLLING_SCHEDULE;
        String subject = A_SUBJECT;
        SelfDescribingPredicate<String> predicate = A_PREDICATE;

        String message = messageThrownBy(() -> expressions.assertThat(schedule, subject, predicate));

        assertThat(message, is(Diagnosis.of(schedule, subject, predicate)));
    }

    @Test
    public void withSubjectFunctionPredicate_returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
                return new PollEvaluationResult<>(null, true);
            }
        };
        expressions.assertThat(A_POLLING_SCHEDULE, A_SUBJECT, A_FUNCTION, A_PREDICATE);
    }

    @Test(expected = AssertionError.class)
    public void withSubjectFunctionPredicate_throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
                return new PollEvaluationResult<>(null, false);
            }
        };
        expressions.assertThat(A_POLLING_SCHEDULE, A_SUBJECT, A_FUNCTION, A_PREDICATE);
    }

    @Test
    public void withSubjectFunctionPredicate_errorMessageIncludesDiagnosis() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
                return new PollEvaluationResult<>(function.apply(subject), false);
            }
        };
        PollingSchedule schedule = A_POLLING_SCHEDULE;
        String subject = A_SUBJECT;
        SelfDescribingFunction<String, String> function = A_FUNCTION;
        SelfDescribingPredicate<String> predicate = A_PREDICATE;
        String functionValue = function.apply(subject);

        String message = messageThrownBy(() -> expressions.assertThat(schedule, subject, function, predicate));

        assertThat(message, is(Diagnosis.of(schedule, subject, function, predicate, functionValue)));
    }

    @Test
    public void withSubjectFunctionMatcher_returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
                return new PollEvaluationResult<>(null, true);
            }
        };
        expressions.assertThat(A_POLLING_SCHEDULE, A_SUBJECT, A_FUNCTION, A_MATCHER);
    }

    @Test(expected = AssertionError.class)
    public void withSubjectFunctionMatcher_throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
                return new PollEvaluationResult<>(null, false);
            }
        };
        expressions.assertThat(A_POLLING_SCHEDULE, A_SUBJECT, A_FUNCTION, A_MATCHER);
    }

    @Test
    public void withSubjectFunctionMatcher_errorMessageIncludesDiagnosis() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
                return new PollEvaluationResult<>(function.apply(subject), false);
            }
        };
        PollingSchedule schedule = A_POLLING_SCHEDULE;
        String subject = A_SUBJECT;
        SelfDescribingFunction<String, String> function = A_FUNCTION;
        Matcher<String> matcher = A_MATCHER;
        String functionValue = function.apply(subject);

        String message = messageThrownBy(() -> expressions.assertThat(schedule, subject, function, matcher));

        assertThat(message, is(Diagnosis.of(schedule, subject, function, matcher, functionValue)));
    }
}
