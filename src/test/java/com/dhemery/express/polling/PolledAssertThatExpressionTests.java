package com.dhemery.express.polling;

import com.dhemery.express.*;
import com.dhemery.express.helpers.DissatisfiedPolledExpressions;
import com.dhemery.express.helpers.SatisfiedPolledExpressions;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.function.Function;

public class PolledAssertThatExpressionTests {
    private static final PollingSchedule IGNORED_POLLING_SCHEDULE = null;
    private static final String IGNORED_SUBJECT = null;
    private static final SelfDescribingBooleanSupplier IGNORED_SUPPLIER = null;
    private static final SelfDescribingPredicate<String> IGNORED_PREDICATE = null;
    private static final SelfDescribingFunction<String,String> IDENTITY = Named.function("identity", Function.identity());
    private static final Matcher<String> IGNORED_MATCHER = null;

    @Test
    public void withBooleanSupplier_returnsWithoutThrowing_ifThePollReturnsTrue() {
        PolledExpressions expressions = new SatisfiedPolledExpressions();
        expressions.assertThat(IGNORED_POLLING_SCHEDULE, IGNORED_SUPPLIER);
    }

    @Test(expected = AssertionError.class)
    public void withBooleanSupplier_throwsAssertionError_ifThePollReturnsFalse() {
        PolledExpressions expressions = new DissatisfiedPolledExpressions();
        expressions.assertThat(IGNORED_POLLING_SCHEDULE, IGNORED_SUPPLIER);
    }

    @Test
    public void withSubjectPredicate_returnsWithoutThrowing_ifThePollReturnsTrue() {
        PolledExpressions expressions = new SatisfiedPolledExpressions();
        expressions.assertThat(IGNORED_POLLING_SCHEDULE, IGNORED_SUBJECT, IGNORED_PREDICATE);
    }

    @Test(expected = AssertionError.class)
    public void withSubjectPredicate_throwsAssertionError_ifThePollReturnsFalse() {
        PolledExpressions expressions = new DissatisfiedPolledExpressions();
        expressions.assertThat(IGNORED_POLLING_SCHEDULE, IGNORED_SUBJECT, IGNORED_PREDICATE);
    }

    @Test
    public void withSubjectFunctionPredicate_returnsWithoutThrowing_ifThePollEvaluationResultIsSatisfied() {
        PolledExpressions expressions = new SatisfiedPolledExpressions();
        expressions.assertThat(IGNORED_POLLING_SCHEDULE, "", IDENTITY, IGNORED_PREDICATE);
    }

    @Test(expected = AssertionError.class)
    public void withSubjectFunctionPredicate_throwsAssertionError_ifThePollEvaluationResultIsDissatisfied() {
        PolledExpressions expressions = new DissatisfiedPolledExpressions();
        expressions.assertThat(IGNORED_POLLING_SCHEDULE, "", IDENTITY, IGNORED_PREDICATE);
    }

    @Test
    public void withSubjectFunctionMatcher_returnsWithoutThrowing_ifThePollEvaluationResultIsSatisfied() {
        PolledExpressions expressions = new SatisfiedPolledExpressions();
        expressions.assertThat(IGNORED_POLLING_SCHEDULE, "", IDENTITY, IGNORED_MATCHER);
    }

    @Test(expected = AssertionError.class)
    public void withSubjectFunctionMatcher_throwsAssertionError_ifThePollEvaluationResultIsDissatisfied() {
        PolledExpressions expressions = new DissatisfiedPolledExpressions();
        expressions.assertThat(IGNORED_POLLING_SCHEDULE, "", IDENTITY, IGNORED_MATCHER);
    }
}
