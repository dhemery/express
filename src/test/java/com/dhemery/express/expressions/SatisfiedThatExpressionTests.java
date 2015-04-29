package com.dhemery.express.expressions;

import com.dhemery.express.Expressions;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SatisfiedThatExpressionTests {
    @Test
    public void withBooleanSupplier_returnsTrue_ifSupplierReturnsTrue() {
        boolean result = Expressions.satisfiedThat(() -> true);
        assertThat(result, is(true));
    }

    @Test
    public void withBooleanSupplier_returnsFalse_ifSupplierReturnsFalse() {
        boolean result = Expressions.satisfiedThat(() -> false);
        assertThat(result, is(false));
    }

    @Test
    public void withSubjectPredicate_returnsTrue_ifPredicateAcceptsSubject() {
        boolean result = Expressions.satisfiedThat("", String::isEmpty);
        assertThat(result, is(true));
    }

    @Test
    public void withSubjectPredicate_returnsFalse_ifPredicateRejectsSubject() {
        boolean result = Expressions.satisfiedThat("foo", String::isEmpty);
        assertThat(result, is(false));
    }

    @Test
    public void withSubjectMatcher_returnsTrue_ifMatcherAcceptsSubject() {
        boolean result = Expressions.satisfiedThat("foo", is("foo"));
        assertThat(result, is(true));
    }

    @Test
    public void withSubjectMatcher_returnsFalse_ifMatcherRejectsSubject() {
        boolean result = Expressions.satisfiedThat("foo", is("food"));
        assertThat(result, is(false));
    }

    @Test
    public void withSubjectFunctionMatcher_returnsTrue_ifMatcherAcceptsFunctionOfSubject() {
        boolean result = Expressions.satisfiedThat("foo", String::toUpperCase, is("FOO"));
        assertThat(result, is(true));
    }

    @Test
    public void withSubjectFunctionMatcher_returnsFalse_ifMatcherRejectsFunctionOfSubject() {
        boolean result = Expressions.satisfiedThat("foo", String::toUpperCase, is("foo"));
        assertThat(result, is(false));
    }

    @Test
    public void withSubjectFunctionPredicate_returnsTrue_ifPredicateAcceptsFunctionOfSubject() {
        boolean result = Expressions.satisfiedThat("foo", String::toUpperCase, "FOO"::equals);
        assertThat(result, is(true));
    }

    @Test
    public void withSubjectFunctionPredicate_returnsFalse_ifPredicateRejectsFunctionOfSubject() {
        boolean result = Expressions.satisfiedThat("foo", String::toUpperCase, "foo"::equals);
        assertThat(result, is(false));
    }
}
