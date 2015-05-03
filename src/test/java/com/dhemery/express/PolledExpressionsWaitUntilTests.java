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

public class PolledExpressionsWaitUntilTests {
    private static final SelfDescribingBooleanSupplier A_BOOLEAN_SUPPLIER = Named.booleanSupplier("boolean supplier", () -> false);
    private static final SelfDescribingFunction<String, String> A_FUNCTION = Named.function("function", Function.identity());
    private static final Matcher<String> A_MATCHER = isEmptyString();
    private static final PollingSchedule A_POLLING_SCHEDULE = new PollingSchedule(Duration.ofMillis(1000), Duration.ofSeconds(60));
    private static final SelfDescribingPredicate<String> A_PREDICATE = Named.predicate("predicate", t -> true);
    private static final String A_SUBJECT = "";

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock Poller poller;

    PolledExpressions expressions;

    @Before
    public void setup() {
        expressions = new ExpressionsPolledBy(poller);
    }

    @Test
    public void defaultScheduleWithBooleanSupplier_returnsWithoutThrowing_ifPollReturnsTrue() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(1), Duration.ofSeconds(2));
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <C extends SelfDescribing & BooleanSupplier>
            boolean poll(PollingSchedule schedule, C supplier) {
                return true;
            }

            @Override
            public PollingSchedule eventually() {
                return defaultSchedule;
            }
        };
        expressions.waitUntil(A_BOOLEAN_SUPPLIER);
    }

    @Test(expected = PollTimeoutException.class)
    public void defaultScheduleWithBooleanSupplier_throwsPollTimeoutException_ifPollReturnsFalse() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(2), Duration.ofSeconds(3));
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <C extends SelfDescribing & BooleanSupplier>
            boolean poll(PollingSchedule schedule, C supplier) {
                return false;
            }

            @Override
            public PollingSchedule eventually() {
                return defaultSchedule;
            }
        };
        expressions.waitUntil(A_BOOLEAN_SUPPLIER);
    }

    @Test
    public void defaultScheduleWithBooleanSupplier_exceptionMessageIncludesDiagnosis() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(3), Duration.ofSeconds(5));
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <C extends SelfDescribing & BooleanSupplier>
            boolean poll(PollingSchedule schedule, C supplier) {
                return false;
            }

            @Override
            public PollingSchedule eventually() {
                return defaultSchedule;
            }
        };
        SelfDescribingBooleanSupplier supplier = A_BOOLEAN_SUPPLIER;

        String message = messageThrownBy(() -> expressions.waitUntil(supplier));

        assertThat(message, is(Diagnosis.of(defaultSchedule, supplier)));
    }

    @Test
    public void defaultScheduleWithSubjectPredicate_returnsWithoutThrowing_ifPollReturnsTrue() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(5), Duration.ofSeconds(8));
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, P extends SelfDescribing & Predicate<? super T>>
            boolean poll(PollingSchedule schedule, T subject, P predicate) {
                return true;
            }

            @Override
            public PollingSchedule eventually() {
                return defaultSchedule;
            }
        };
        expressions.waitUntil(A_SUBJECT, A_PREDICATE);
    }

    @Test(expected = PollTimeoutException.class)
    public void defaultScheduleWithSubjectPredicate_throwsPollTimeoutException_ifPollReturnsFalse() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(8), Duration.ofSeconds(13));
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, P extends SelfDescribing & Predicate<? super T>>
            boolean poll(PollingSchedule schedule, T subject, P predicate) {
                return false;
            }

            @Override
            public PollingSchedule eventually() {
                return defaultSchedule;
            }
        };
        expressions.waitUntil(A_SUBJECT, A_PREDICATE);
    }

    @Test
    public void defaultScheduleWithSubjectPredicate_exceptionMessageIncludesDiagnosis() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(13), Duration.ofSeconds(21));
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, P extends SelfDescribing & Predicate<? super T>>
            boolean poll(PollingSchedule schedule, T subject, P predicate) {
                return false;
            }

            @Override
            public PollingSchedule eventually() {
                return defaultSchedule;
            }
        };
        String subject = A_SUBJECT;
        SelfDescribingPredicate<String> predicate = A_PREDICATE;

        String message = messageThrownBy(() -> expressions.waitUntil(subject, predicate));

        assertThat(message, is(Diagnosis.of(defaultSchedule, subject, predicate)));
    }

    @Test
    public void defaultScheduleWithSubjectFunctionPredicate_returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(21), Duration.ofSeconds(34));
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
                return new PollEvaluationResult<>(null, true);
            }

            @Override
            public PollingSchedule eventually() {
                return defaultSchedule;
            }
        };
        expressions.waitUntil(A_SUBJECT, A_FUNCTION, A_PREDICATE);
    }

    @Test(expected = PollTimeoutException.class)
    public void defaultScheduleWithSubjectFunctionPredicate_throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(34), Duration.ofSeconds(55));
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
                return new PollEvaluationResult<>(null, false);
            }

            @Override
            public PollingSchedule eventually() {
                return defaultSchedule;
            }
        };
        expressions.waitUntil(A_SUBJECT, A_FUNCTION, A_PREDICATE);
    }

    @Test
    public void defaultScheduleWithSubjectFunctionPredicate_exceptionMessageIncludesDiagnosis() {
        PollingSchedule defaultSchedule = new PollingSchedule(Duration.ofSeconds(55), Duration.ofSeconds(89));
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
                return new PollEvaluationResult<>(function.apply(subject), false);
            }

            @Override
            public PollingSchedule eventually() {
                return defaultSchedule;
            }
        };
        String subject = A_SUBJECT;
        SelfDescribingFunction<String, String> function = A_FUNCTION;
        SelfDescribingPredicate<String> predicate = A_PREDICATE;
        String functionValue = function.apply(subject);

        String message = messageThrownBy(() -> expressions.waitUntil(subject, function, predicate));

        assertThat(message, is(Diagnosis.of(defaultSchedule, subject, function, predicate, functionValue)));
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
        expressions.waitUntil(A_SUBJECT, A_FUNCTION, A_MATCHER);
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
        expressions.waitUntil(A_SUBJECT, A_FUNCTION, A_MATCHER);
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
        String subject = A_SUBJECT;
        SelfDescribingFunction<String, String> function = A_FUNCTION;
        Matcher<String> matcher = A_MATCHER;
        String functionValue = function.apply(subject);

        String message = messageThrownBy(() -> expressions.waitUntil(subject, function, matcher));

        assertThat(message, is(Diagnosis.of(defaultSchedule, subject, function, matcher, functionValue)));
    }

    @Test
    public void scheduleWithBooleanSupplier_returnsWithoutThrowing_ifPollReturnsTrue() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <C extends SelfDescribing & BooleanSupplier>
            boolean poll(PollingSchedule schedule, C supplier) {
                return true;
            }
        };
        expressions.waitUntil(A_POLLING_SCHEDULE, A_BOOLEAN_SUPPLIER);
    }

    @Test(expected = PollTimeoutException.class)
    public void scheduleWithBooleanSupplier_throwsPollTimeoutException_ifPollReturnsFalse() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <C extends SelfDescribing & BooleanSupplier>
            boolean poll(PollingSchedule schedule, C supplier) {
                return false;
            }
        };
        expressions.waitUntil(A_POLLING_SCHEDULE, A_BOOLEAN_SUPPLIER);
    }

    @Test
    public void scheduleWithBooleanSupplier_exceptionMessageIncludesDiagnosis() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <C extends SelfDescribing & BooleanSupplier>
            boolean poll(PollingSchedule schedule, C supplier) {
                return false;
            }
        };
        PollingSchedule schedule = A_POLLING_SCHEDULE;
        SelfDescribingBooleanSupplier supplier = A_BOOLEAN_SUPPLIER;

        String message = messageThrownBy(() -> expressions.waitUntil(schedule, supplier));

        assertThat(message, is(Diagnosis.of(schedule, supplier)));
    }

    @Test
    public void scheduleWithSubjectPredicate_returnsWithoutThrowing_ifPollReturnsTrue() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, P extends SelfDescribing & Predicate<? super T>>
            boolean poll(PollingSchedule schedule, T subject, P predicate) {
                return true;
            }
        };
        expressions.waitUntil(A_POLLING_SCHEDULE, A_SUBJECT, A_PREDICATE);
    }

    @Test(expected = PollTimeoutException.class)
    public void scheduleWithSubjectPredicate_throwsPollTimeoutException_ifPollReturnsFalse() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, P extends SelfDescribing & Predicate<? super T>>
            boolean poll(PollingSchedule schedule, T subject, P predicate) {
                return false;
            }
        };
        expressions.waitUntil(A_POLLING_SCHEDULE, A_SUBJECT, A_PREDICATE);
    }

    @Test
    public void scheduleWithSubjectPredicate_exceptionMessageIncludesDiagnosis() {
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

        String message = messageThrownBy(() -> expressions.waitUntil(schedule, subject, predicate));

        assertThat(message, is(Diagnosis.of(schedule, subject, predicate)));
    }

    @Test
    public void scheduleWithSubjectFunctionPredicate_returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
                return new PollEvaluationResult<>(null, true);
            }
        };
        expressions.waitUntil(A_POLLING_SCHEDULE, A_SUBJECT, A_FUNCTION, A_PREDICATE);
    }

    @Test(expected = PollTimeoutException.class)
    public void scheduleWithSubjectFunctionPredicate_throwsPollTimeoutException_ifPollEvaluationResultIsDissatisfied() {
        PolledExpressions expressions = new ExpressionsPolledBy(poller) {
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
                return new PollEvaluationResult<>(null, false);
            }
        };
        expressions.waitUntil(A_POLLING_SCHEDULE, A_SUBJECT, A_FUNCTION, A_PREDICATE);
    }

    @Test
    public void scheduleWithSubjectFunctionPredicate_exceptionMessageIncludesDiagnosis() {
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

        String message = messageThrownBy(() -> expressions.waitUntil(schedule, subject, function, predicate));

        assertThat(message, is(Diagnosis.of(schedule, subject, function, predicate, functionValue)));
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
        expressions.waitUntil(A_POLLING_SCHEDULE, A_SUBJECT, A_FUNCTION, A_MATCHER);
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
        expressions.waitUntil(A_POLLING_SCHEDULE, A_SUBJECT, A_FUNCTION, A_MATCHER);
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
        PollingSchedule schedule = A_POLLING_SCHEDULE;
        String subject = A_SUBJECT;
        SelfDescribingFunction<String, String> function = A_FUNCTION;
        Matcher<String> matcher = A_MATCHER;
        String functionValue = function.apply(subject);

        String message = messageThrownBy(() -> expressions.waitUntil(schedule, subject, function, matcher));

        assertThat(message, is(Diagnosis.of(schedule, subject, function, matcher, functionValue)));
    }
}
