package com.dhemery.express.expressions;

import com.dhemery.express.*;
import com.dhemery.express.helpers.Throwables;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
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
    public void withBooleanSupplier_diagnosisDescribes_supplier() {
        SelfDescribingBooleanSupplier supplier = Named.booleanSupplier("supplier name", () -> false);

        String[] expectedLines = new String[]{
                "",
                "Expected: " + StringDescription.toString(supplier)
        };

        String[] lines = Throwables.messageOfAssertionErrorThrownBy(() -> Expressions.assertThat(supplier));

        assertThat(lines, is(arrayContaining(expectedLines)));
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
    public void withSubjectPredicate_diagnosisDescribes_predicate_subject() {
        SelfDescribingPredicate<String> predicate = Named.predicate("an empty string", String::isEmpty);
        String subject = "subject";

        String[] expectedLines = new String[]{
                "",
                "Expected: " + StringDescription.toString(predicate),
                "     but: was " + new StringDescription().appendValue(subject)
        };

        String[] lines = Throwables.messageOfAssertionErrorThrownBy(() -> Expressions.assertThat(subject, predicate));

        assertThat(lines, is(arrayContaining(expectedLines)));
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

        Description mismatchDescription = new StringDescription();
        matcher.describeMismatch(subject, mismatchDescription);
        String[] expectedLines = new String[]{
                "",
                "Expected: " + StringDescription.toString(matcher),
                "     but: " + mismatchDescription
        };

        String[] lines = Throwables.messageOfAssertionErrorThrownBy(() -> Expressions.assertThat(subject, matcher));

        assertThat(lines, is(arrayContaining(expectedLines)));
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
        Description mismatchDescription = new StringDescription();
        matcher.describeMismatch(functionValue, mismatchDescription);

        String[] expectedLines = new String[]{
                subject,
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(matcher),
                "     but: " + mismatchDescription
        };

        String[] lines = Throwables.messageOfAssertionErrorThrownBy(() -> Expressions.assertThat(subject, function, matcher));
        assertThat(lines, is(arrayContaining(expectedLines)));
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

        String[] expectedLines = new String[]{
                subject,
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(predicate),
                "     but: was " + new StringDescription().appendValue(functionValue)
        };

        String[] lines = Throwables.messageOfAssertionErrorThrownBy(() -> Expressions.assertThat(subject, function, predicate));
        assertThat(lines, is(arrayContaining(expectedLines)));
    }

}
