package com.dhemery.express.expressions;

import com.dhemery.express.*;
import com.dhemery.express.helpers.Throwables;
import org.hamcrest.Matcher;
import org.junit.Test;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AssertThatExpressionTests {
    @Test
    public void withBooleanSupplier_returnsWithoutThrowing_ifSupplierReturnsTrue() {
        Expressions.assertThat(Named.booleanSupplier("supplier", () -> true));
    }

    @Test(expected = AssertionError.class)
    public void withBooleanSupplier_throwsAssertionError_ifSupplierReturnsFalse() {
        Expressions.assertThat(Named.booleanSupplier("supplier", () -> false));
    }

    @Test
    public void withBooleanSupplier_assertionErrorIncludesDiagnosis() {
        SelfDescribingBooleanSupplier supplier = Named.booleanSupplier("supplier name", () -> false);

        String message = messageThrownBy(() -> Expressions.assertThat(supplier));

        assertThat(message, is(Diagnosis.of(supplier)));
    }

    @Test
    public void withSubjectPredicate_returnsWithoutThrowing_IfPredicateAcceptsSubject() {
        Expressions.assertThat("", Named.predicate("is empty", String::isEmpty));
    }

    @Test(expected = AssertionError.class)
    public void withSubjectPredicate_throwsAssertionError_ifPredicateRejectsSubject() {
        Expressions.assertThat("not empty", Named.predicate("is empty", String::isEmpty));
    }

    @Test
    public void withSubjectPredicate_assertionErrorIncludesDiagnosis() {
        SelfDescribingPredicate<String> predicate = Named.predicate("an empty string", String::isEmpty);
        String subject = "subject";

        String message = messageThrownBy(() -> Expressions.assertThat(subject, predicate));

        assertThat(message, is(Diagnosis.of(subject, predicate)));
    }

    @Test
    public void withSubjectMatcher_returnsWithoutThrowing_ifMatcherAcceptsSubject() {
        Expressions.assertThat("foo", is("foo"));
    }

    @Test(expected = AssertionError.class)
    public void withSubjectMatcher_throwsAssertionError_ifMatcherRejectsSubject() {
        Expressions.assertThat("foo", is("bar"));
    }

    @Test
    public void withSubjectMatcher_diagnosisDescribes_matcher_mismatchOfSubject() {
        String subject = "subject";
        Matcher<String> matcher = is("foo");

        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(subject, matcher));

        assertThat(message, is(Diagnosis.of(subject, matcher)));
    }

    @Test
    public void withSubjectFunctionMatcher_returnsWithoutThrowing_ifMatcherAcceptsFunctionOfSubject() {
        Expressions.assertThat("foo", Named.function("to upper case", String::toUpperCase), is("FOO"));
    }

    @Test(expected = AssertionError.class)
    public void withSubjectFunctionMatcher_throwsAssertionError_ifMatcherRejectsFunctionOfSubject() {
        Expressions.assertThat("foo", Named.function("to upper case", String::toUpperCase), is("bar"));
    }

    @Test
    public void withSubjectFunctionMatcher_diagnosisDescribes_subject_matcher_function_mismatchOfFunctionResult() {
        String subject = "subject";
        SelfDescribingFunction<String, String> function = Named.function("upper case", String::toUpperCase);
        Matcher<String> matcher = is("bar");
        String functionValue = function.apply(subject);

        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(subject, function, matcher));

        assertThat(message, is(Diagnosis.of(subject, function, matcher, functionValue)));
    }

    @Test
    public void withSubjectFunctionPredicate_returnsWithoutThrowing_ifPredicateAcceptsFunctionOfSubject() {
        Expressions.assertThat("foo", Named.function("to upper case", String::toUpperCase), Named.predicate("is \"FOO\"", "FOO"::equals));
    }

    @Test(expected = AssertionError.class)
    public void withSubjectFunctionPredicate_throwsAssertionError_ifPredicateRejectsFunctionOfSubject() {
        Expressions.assertThat("foo", Named.function("to upper case", String::toUpperCase), Named.predicate("is \"foo\"", "foo"::equals));
    }

    @Test
    public void withSubjectFunctionPredicate_diagnosisDescribes_subject_predicate_function_functionResult() {
        String subject = "subject";
        SelfDescribingFunction<String, String> function = Named.function("upper case", String::toUpperCase);
        SelfDescribingPredicate<String> predicate = Named.predicate("equals \"bar\"", "bar"::equals);
        String functionValue = function.apply(subject);

        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(subject, function, predicate));

        assertThat(message, is(Diagnosis.of(subject, function, predicate, functionValue)));
    }
}
