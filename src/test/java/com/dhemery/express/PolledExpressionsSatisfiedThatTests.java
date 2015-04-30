package com.dhemery.express;

import com.dhemery.express.*;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.junit.Test;

import java.time.Duration;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;

public class PolledExpressionsSatisfiedThatTests {
    private static final SelfDescribingBooleanSupplier A_BOOLEAN_SUPPLIER = Named.booleanSupplier("boolean supplier", () -> false);
    private static final SelfDescribingFunction<String, String> A_FUNCTION = Named.function("function", Function.identity());
    private static final Matcher<String> A_MATCHER = isEmptyString();
    private static final PollingSchedule A_POLLING_SCHEDULE = new PollingSchedule(Duration.ofMillis(1000), Duration.ofSeconds(60));
    private static final SelfDescribingPredicate<String> A_PREDICATE = Named.predicate("predicate", t -> true);
    private static final String A_SUBJECT = "";

    @Test
    public void withBooleanSupplier_returnTrue_ifPollReturnsTrue() {
        PolledExpressions expressions = new PolledExpressions() {
            @Override
            public <C extends SelfDescribing & BooleanSupplier>
            boolean poll(PollingSchedule schedule, C supplier) {
                return true;
            }
        };
        boolean result = expressions.satisfiedThat(A_POLLING_SCHEDULE, A_BOOLEAN_SUPPLIER);
        assertThat(result, is(true));
    }

    @Test
    public void withBooleanSupplier_returnsFalse_ifPollReturnsFalse() {
        PolledExpressions expressions = new PolledExpressions() {
            @Override
            public <C extends SelfDescribing & BooleanSupplier>
            boolean poll(PollingSchedule schedule, C supplier) {
                return true;
            }
        };
        boolean result = expressions.satisfiedThat(A_POLLING_SCHEDULE, A_BOOLEAN_SUPPLIER);
        assertThat(result, is(true));
    }

    @Test
    public void withSubjectPredicate_returnsTrue_ifPollReturnsTrue() {
        PolledExpressions expressions = new PolledExpressions() {
            @Override
            public <T, P extends SelfDescribing & Predicate<? super T>>
            boolean poll(PollingSchedule schedule, T subject, P predicate) {
                return true;
            }
        };
        boolean result = expressions.satisfiedThat(A_POLLING_SCHEDULE, A_SUBJECT, A_PREDICATE);
        assertThat(result, is(true));
    }

    @Test
    public void withSubjectPredicate_returnsFalse_ifPollReturnsFalse() {
        PolledExpressions expressions = new PolledExpressions() {
            @Override
            public <T, P extends SelfDescribing & Predicate<? super T>>
            boolean poll(PollingSchedule schedule, T subject, P predicate) {
                return false;
            }
        };
        boolean result = expressions.satisfiedThat(A_POLLING_SCHEDULE, A_SUBJECT, A_PREDICATE);
        assertThat(result, is(false));
    }

    @Test
    public void withSubjectFunctionPredicate_returnsTrue_ifPollEvaluationResultIsSatisfied() {
        PolledExpressions expressions = new PolledExpressions(){
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
                return new PollEvaluationResult<>(null, true);
            }
        };
        boolean result = expressions.satisfiedThat(A_POLLING_SCHEDULE, A_SUBJECT, A_FUNCTION, A_PREDICATE);
        assertThat(result, is(true));
    }

    @Test
    public void withSubjectFunctionPredicate_returnsFalse_ifPollEvaluationResultIsDissatisfied() {
        PolledExpressions expressions = new PolledExpressions(){
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate) {
                return new PollEvaluationResult<>(null, false);
            }
        };

        boolean result = expressions.satisfiedThat(A_POLLING_SCHEDULE, A_SUBJECT, A_FUNCTION, A_PREDICATE);
        assertThat(result, is(false));
    }

    @Test
    public void withSubjectFunctionMatcher_returnsTrue_ifPollEvaluationResultIsSatisfied() {
        PolledExpressions expressions = new PolledExpressions(){
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
                return new PollEvaluationResult<>(null, true);
            }
        };
        boolean result = expressions.satisfiedThat(A_POLLING_SCHEDULE, A_SUBJECT, A_FUNCTION, A_MATCHER);
        assertThat(result, is(true));
    }

    @Test
    public void withSubjectFunctionMatcher_returnsFalse_ifPollEvaluationResultIsDissatisfied() {
        PolledExpressions expressions = new PolledExpressions(){
            @Override
            public <T, R, F extends SelfDescribing & Function<? super T, R>>
            PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
                return new PollEvaluationResult<>(null, false);
            }
        };
        boolean result = expressions.satisfiedThat(A_POLLING_SCHEDULE, A_SUBJECT, A_FUNCTION, A_MATCHER);
        assertThat(result, is(false));
    }
}
