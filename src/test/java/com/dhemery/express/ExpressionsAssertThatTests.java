package com.dhemery.express;

import com.dhemery.express.helpers.Throwables;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.dhemery.express.helpers.Actions.appendItsStringValue;
import static com.dhemery.express.helpers.Actions.appendTheMismatchDescriptionOfTheItem;
import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class ExpressionsAssertThatTests {
    public static class WithBooleanSupplier {
        @Rule
        public JUnitRuleMockery context = new JUnitRuleMockery();

        @Mock
        SelfDescribingBooleanSupplier supplier;

        @Before
        public void setup() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(any(SelfDescribing.class)).method("describeTo");
                    will(appendItsStringValue());
            }}); //@formatter:on
        }

        @Test
        public void returnsWithoutThrowing_ifSupplierReturnsTrue() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(supplier).getAsBoolean();
                    will(returnValue(true));
            }}); //@formatter:on

            Expressions.assertThat(supplier);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifSupplierReturnsFalse() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(supplier).getAsBoolean();
                    will(returnValue(false));
            }}); //@formatter:on

            Expressions.assertThat(supplier);
        }

        @Test
        public void errorMessageIncludesDiagnosis() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(supplier).getAsBoolean();
                    will(returnValue(false));
            }}); //@formatter:on

            String message = messageThrownBy(() -> Expressions.assertThat(supplier));

            assertThat(message, is(Diagnosis.of(supplier)));
        }
    }

    public static class WithSubjectPredicate {
        @Rule
        public JUnitRuleMockery context = new JUnitRuleMockery();

        private final String subject = "subject";
        @Mock
        SelfDescribingPredicate<String> predicate;

        @Before
        public void setup() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(any(SelfDescribing.class)).method("describeTo");
                    will(appendItsStringValue());
            }}); //@formatter:on
        }

        @Test
        public void withSubjectPredicate_returnsWithoutThrowing_IfPredicateAcceptsSubject() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(predicate).test(subject);
                    will(returnValue(true));
            }}); //@formatter:on

            Expressions.assertThat(subject, predicate);
        }

        @Test(expected = AssertionError.class)
        public void withSubjectPredicate_throwsAssertionError_ifPredicateRejectsSubject() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(predicate).test(subject);
                    will(returnValue(false));
            }}); //@formatter:on

            Expressions.assertThat(subject, predicate);
        }

        @Test
        public void withSubjectPredicate_errorMessageIncludesDiagnosis() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(predicate).test(subject);
                    will(returnValue(false));
            }}); //@formatter:on

            String message = messageThrownBy(() -> Expressions.assertThat(subject, predicate));

            assertThat(message, is(Diagnosis.of(subject, predicate)));
        }
    }

    public static class WithSubjectMatcher {
        @Rule
        public JUnitRuleMockery context = new JUnitRuleMockery();

        private final String subject = "subject";
        @Mock
        Matcher<String> matcher;

        @Before
        public void setup() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(any(SelfDescribing.class)).method("describeTo");
                    will(appendItsStringValue());
                allowing(same(matcher)).method("describeMismatch").with(same(subject), any(Description.class));
                    will(appendTheMismatchDescriptionOfTheItem());
            }}); //@formatter:on
        }

        @Test
        public void withSubjectMatcher_returnsWithoutThrowing_ifMatcherAcceptsSubject() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(same(matcher)).method("matches").with(same(subject));
                    will(returnValue(true));
            }}); //@formatter:on

            Expressions.assertThat(subject, matcher);
        }

        @Test(expected = AssertionError.class)
        public void withSubjectMatcher_throwsAssertionError_ifMatcherRejectsSubject() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(same(matcher)).method("matches").with(same(subject));
                    will(returnValue(false));
            }}); //@formatter:on

            Expressions.assertThat(subject, matcher);
        }

        @Test
        public void withSubjectMatcher_diagnosisDescribes_matcher_mismatchOfSubject() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(same(matcher)).method("matches").with(same(subject));
                    will(returnValue(false));
            }}); //@formatter:on

            String message = Throwables.messageThrownBy(() -> Expressions.assertThat(subject, matcher));

            assertThat(message, is(Diagnosis.of(subject, matcher)));
        }
    }

    public static class WithSubjectFunctionMatcher {
        @Rule
        public JUnitRuleMockery context = new JUnitRuleMockery();

        private final String subject = "subject";
        @Mock
        Matcher<String> matcher;
        @Mock
        SelfDescribingFunction<String, String> function;
        private final String functionValue = "function value";

        @Before
        public void setup() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(any(SelfDescribing.class)).method("describeTo");
                    will(appendItsStringValue());
                allowing(same(matcher)).method("describeMismatch").with(same(functionValue), any(Description.class));
                    will(appendTheMismatchDescriptionOfTheItem());
                allowing(function).apply(subject);
                    will(returnValue(functionValue));
            }}); //@formatter:on
        }

        @Test
        public void withSubjectFunctionMatcher_returnsWithoutThrowing_ifMatcherAcceptsFunctionOfSubject() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(same(matcher)).method("matches").with(same(functionValue));
                    will(returnValue(true));
            }}); //@formatter:on

            Expressions.assertThat(subject, function, matcher);
        }

        @Test(expected = AssertionError.class)
        public void withSubjectFunctionMatcher_throwsAssertionError_ifMatcherRejectsFunctionOfSubject() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(same(matcher)).method("matches").with(same(functionValue));
                    will(returnValue(false));
            }}); //@formatter:on

            Expressions.assertThat(subject, function, matcher);
        }

        @Test
        public void withSubjectFunctionMatcher_diagnosisDescribes_subject_matcher_function_mismatchOfFunctionResult() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(same(matcher)).method("matches").with(same(functionValue));
                    will(returnValue(false));
            }}); //@formatter:on

            String message = Throwables.messageThrownBy(() -> Expressions.assertThat(subject, function, matcher));

            assertThat(message, is(Diagnosis.of(subject, function, matcher, functionValue)));
        }
    }

    public static class WithSubjectFunctionPredicate {
        @Rule
        public JUnitRuleMockery context = new JUnitRuleMockery();

        private final String subject = "subject";
        @Mock
        SelfDescribingFunction<String, String> function;
        @Mock
        SelfDescribingPredicate<String> predicate;
        private final String functionValue = "function value";

        @Before
        public void setup() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(any(SelfDescribing.class)).method("describeTo");
                    will(appendItsStringValue());
                allowing(function).apply(subject);
                    will(returnValue(functionValue));
            }}); //@formatter:on
        }

        @Test
        public void withSubjectFunctionPredicate_returnsWithoutThrowing_ifPredicateAcceptsFunctionOfSubject() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(predicate).test(functionValue);
                    will(returnValue(true));
            }}); //@formatter:on

            Expressions.assertThat(subject, function, predicate);
        }

        @Test(expected = AssertionError.class)
        public void withSubjectFunctionPredicate_throwsAssertionError_ifPredicateRejectsFunctionOfSubject() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(predicate).test(functionValue);
                    will(returnValue(false));
            }}); //@formatter:on

            Expressions.assertThat(subject, function, predicate);
        }

        @Test
        public void withSubjectFunctionPredicate_diagnosisDescribes_subject_predicate_function_functionResult() {
            context.checking(new Expectations() {{ //@formatter:off
                allowing(predicate).test(functionValue);
                    will(returnValue(false));
            }}); //@formatter:on

            String message = Throwables.messageThrownBy(() -> Expressions.assertThat(subject, function, predicate));

            assertThat(message, is(Diagnosis.of(subject, function, predicate, functionValue)));
        }

    }

}