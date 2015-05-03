package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionTestBase;
import com.dhemery.express.helpers.PolledExpressionTestBase;
import com.dhemery.express.helpers.PollingSchedules;
import com.dhemery.express.helpers.Throwables;
import org.hamcrest.Matcher;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.dhemery.express.helpers.FunctionExpectations.*;
import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class AssertThatTests {
    public static class WithBooleanSupplier extends ExpressionTestBase {

        @Mock
        SelfDescribingBooleanSupplier supplier;

        @Test
        public void returnsWithoutThrowing_ifSupplierReturnsTrue() {
            givenThat(getAsBooleanReturns(supplier, true));

            Expressions.assertThat(supplier);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifSupplierReturnsFalse() {
            givenThat(getAsBooleanReturns(supplier, false));

            Expressions.assertThat(supplier);
        }

        @Test
        public void errorMessageIncludesDiagnosis() {
            givenThat(getAsBooleanReturns(supplier, false));

            String message = messageThrownBy(() -> Expressions.assertThat(supplier));

            assertThat(message, is(Diagnosis.of(supplier)));
        }
    }

    public static class WithSubjectPredicate extends ExpressionTestBase {
        private static final String SUBJECT = "subject";

        @Mock
        SelfDescribingPredicate<String> predicate;

        @Test
        public void returnsWithoutThrowing_ifMatcherAcceptsSubject() {
            givenThat(testReturns(predicate, SUBJECT, true));

            Expressions.assertThat(SUBJECT, predicate);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifMatcherRejectsSubject() {
            givenThat(testReturns(predicate, SUBJECT, false));

            Expressions.assertThat(SUBJECT, predicate);
        }

        @Test
        public void diagnosisDescribes_matcher_mismatchOfSubject() {
            givenThat(testReturns(predicate, SUBJECT, false));

            String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, predicate));

            assertThat(message, is(Diagnosis.of(SUBJECT, predicate)));
        }
    }

    public static class WithSubjectMatcher extends ExpressionTestBase {
        private static final String SUBJECT = "subject";

        @Mock
        Matcher<String> matcher;

        @Test
        public void returnsWithoutThrowing_ifMatcherAcceptsSubject() {
            givenThat(matchesReturns(matcher, SUBJECT, true));

            Expressions.assertThat(SUBJECT, matcher);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifMatcherRejectsSubject() {
            givenThat(matchesReturns(matcher, SUBJECT, false));

            Expressions.assertThat(SUBJECT, matcher);
        }

        @Test
        public void diagnosisDescribes_matcher_mismatchOfSubject() {
            givenThat(matchesReturns(matcher, SUBJECT, false));

            String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, matcher));

            assertThat(message, is(Diagnosis.of(SUBJECT, matcher)));
        }
    }

    public static class WithSubjectFunctionPredicate extends ExpressionTestBase {
        private static final String SUBJECT = "subject";
        private static final String FUNCTION_VALUE = "function value";
        @Mock
        SelfDescribingFunction<String, String> function;

        @Mock
        SelfDescribingPredicate<String> predicate;

        @Before
        public void setup() {
            givenThat(applyReturns(function, SUBJECT, FUNCTION_VALUE));
        }

        @Test
        public void withSubjectFunctionPredicate_returnsWithoutThrowing_ifPredicateAcceptsFunctionOfSubject() {
            givenThat(testReturns(predicate, FUNCTION_VALUE, true));

            Expressions.assertThat(SUBJECT, function, predicate);
        }

        @Test(expected = AssertionError.class)
        public void withSubjectFunctionPredicate_throwsAssertionError_ifPredicateRejectsFunctionOfSubject() {
            givenThat(testReturns(predicate, FUNCTION_VALUE, false));

            Expressions.assertThat(SUBJECT, function, predicate);
        }

        @Test
        public void withSubjectFunctionPredicate_diagnosisDescribes_subject_predicate_function_functionResult() {
            givenThat(testReturns(predicate, FUNCTION_VALUE, false));

            String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, function, predicate));

            assertThat(message, is(Diagnosis.of(SUBJECT, function, predicate, FUNCTION_VALUE)));
        }
    }

    public static class WithSubjectFunctionMatcher extends ExpressionTestBase {
        private static final String SUBJECT = "subject";

        private static final String FUNCTION_VALUE = "function value";
        @Mock
        SelfDescribingFunction<String, String> function;

        @Mock
        Matcher<String> matcher;

        @Before
        public void setup() {
            givenThat(applyReturns(function, SUBJECT, FUNCTION_VALUE));
        }

        @Test
        public void returnsWithoutThrowing_ifMatcherAcceptsFunctionOfSubject() {
            givenThat(matchesReturns(matcher, FUNCTION_VALUE, true));

            Expressions.assertThat(SUBJECT, function, matcher);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifMatcherRejectsFunctionOfSubject() {
            givenThat(matchesReturns(matcher, FUNCTION_VALUE, false));

            Expressions.assertThat(SUBJECT, function, matcher);
        }

        @Test
        public void messageIncludesDiagnosis() {
            givenThat(matchesReturns(matcher, FUNCTION_VALUE, false));

            String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, function, matcher));

            assertThat(message, is(Diagnosis.of(SUBJECT, function, matcher, FUNCTION_VALUE)));
        }

    }

    public static class PolledWithBooleanSupplier extends PolledExpressionTestBase {
        @Mock
        SelfDescribingBooleanSupplier supplier;
        PollingSchedule schedule = PollingSchedules.random();

        @Test
        public void returnsWithoutThrowing_ifPollReturnsTrue() {
            givenThat(pollReturns(schedule, supplier, true));

            expressions.assertThat(schedule, supplier);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifPollReturnsFalse() {
            givenThat(pollReturns(schedule, supplier, false));

            expressions.assertThat(schedule, supplier);
        }

        @Test
        public void errorMessageIncludesDiagnosis() {
            givenThat(pollReturns(schedule, supplier, false));

            String message = messageThrownBy(() -> expressions.assertThat(schedule, supplier));

            assertThat(message, is(Diagnosis.of(schedule, supplier)));
        }
    }

    public static class PolledWithSubjectPredicate extends PolledExpressionTestBase {
        private static final String subject = "subject";
        private final PollingSchedule schedule = PollingSchedules.random();

        @Mock
        SelfDescribingPredicate<String> predicate;

        @Test
        public void returnsWithoutThrowing_ifPollReturnsTrue() {
            givenThat(pollReturns(schedule, subject, predicate, true));

            expressions.assertThat(schedule, subject, predicate);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifPollReturnsFalse() {
            givenThat(pollReturns(schedule, subject, predicate, false));

            expressions.assertThat(schedule, subject, predicate);
        }

        @Test
        public void errorMessageIncludesDiagnosis() {
            givenThat(pollReturns(schedule, subject, predicate, false));

            String message = messageThrownBy(() -> expressions.assertThat(schedule, subject, predicate));

            assertThat(message, is(Diagnosis.of(schedule, subject, predicate)));
        }
    }

    public static class PolledWithSubjectFunctionMatcher extends PolledExpressionTestBase {
        private static final String SUBJECT = "subject";
        private static final String FUNCTION_VALUE = "function value";
        private final PollingSchedule schedule = PollingSchedules.random();

        @Mock
        SelfDescribingFunction<String, String> function;
        @Mock
        Matcher<String> matcher;

        @Before
        public void setup() {
            givenThat(applyReturns(function, SUBJECT, FUNCTION_VALUE));
        }

        @Test
        public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            givenThat(pollReturns(schedule, SUBJECT, function, matcher, new PollEvaluationResult<>(FUNCTION_VALUE, true)));

            expressions.assertThat(schedule, SUBJECT, function, matcher);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
            givenThat(pollReturns(schedule, SUBJECT, function, matcher, new PollEvaluationResult<>(FUNCTION_VALUE, false)));

            expressions.assertThat(schedule, SUBJECT, function, matcher);
        }

        @Test
        public void errorMessageIncludesDiagnosis() {
            givenThat(pollReturns(schedule, SUBJECT, function, matcher, new PollEvaluationResult<>(FUNCTION_VALUE, false)));

            String message = messageThrownBy(() -> expressions.assertThat(schedule, SUBJECT, function, matcher));

            assertThat(message, is(Diagnosis.of(schedule, SUBJECT, function, matcher, FUNCTION_VALUE)));
        }
    }

    public static class PolledWithSubjectFunctionPredicate extends PolledExpressionTestBase {
        private static final String SUBJECT = "subject";
        private static final String FUNCTION_VALUE = "function value";
        private final PollingSchedule schedule = PollingSchedules.random();

        @Mock
        SelfDescribingFunction<String, String> function;
        @Mock
        SelfDescribingPredicate<String> predicate;

        @Before
        public void setup() {
            givenThat(applyReturns(function, SUBJECT, FUNCTION_VALUE));
        }

        @Test
        public void returnsWithoutThrowing_ifPollEvaluationResultIsSatisfied() {
            givenThat(pollReturns(schedule, SUBJECT, function, predicate, new PollEvaluationResult<>(FUNCTION_VALUE, true)));

            expressions.assertThat(schedule, SUBJECT, function, predicate);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifPollEvaluationResultIsDissatisfied() {
            givenThat(pollReturns(schedule, SUBJECT, function, predicate, new PollEvaluationResult<>(FUNCTION_VALUE, false)));

            expressions.assertThat(schedule, SUBJECT, function, predicate);
        }

        @Test
        public void errorMessageIncludesDiagnosis() {
            givenThat(pollReturns(schedule, SUBJECT, function, predicate, new PollEvaluationResult<>(FUNCTION_VALUE, false)));


            String message = messageThrownBy(() -> expressions.assertThat(schedule, SUBJECT, function, predicate));

            assertThat(message, is(Diagnosis.of(schedule, SUBJECT, function, predicate, FUNCTION_VALUE)));
        }
    }
}
