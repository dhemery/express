package com.dhemery.express;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class SatisfiedThatTests {
    private static final String SUBJECT = "foo";
    private static final String FUNCTION_VALUE = "bar";

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
        boolean result = Expressions.satisfiedThat(SUBJECT, s -> true);
        assertThat(result, is(true));
    }

    @Test
    public void withSubjectPredicate_returnsFalse_ifPredicateRejectsSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, s -> false);
        assertThat(result, is(false));
    }

    @Test
    public void withSubjectMatcher_returnsTrue_ifMatcherAcceptsSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, equalTo(SUBJECT));
        assertThat(result, is(true));
    }

    @Test
    public void withSubjectMatcher_returnsFalse_ifMatcherRejectsSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, equalTo(SUBJECT + "!"));
        assertThat(result, is(false));
    }

    @Test
    public void withSubjectFunctionMatcher_returnsTrue_ifMatcherAcceptsFunctionOfSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, s -> FUNCTION_VALUE, equalTo(FUNCTION_VALUE));
        assertThat(result, is(true));
    }

    @Test
    public void withSubjectFunctionMatcher_returnsFalse_ifMatcherRejectsFunctionOfSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, s -> FUNCTION_VALUE, equalTo(FUNCTION_VALUE + "!"));
        assertThat(result, is(false));
    }

    @Test
    public void withSubjectFunctionPredicate_returnsTrue_ifPredicateAcceptsFunctionOfSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, s -> FUNCTION_VALUE, s -> true);
        assertThat(result, is(true));
    }

    @Test
    public void withSubjectFunctionPredicate_returnsFalse_ifPredicateRejectsFunctionOfSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, s -> FUNCTION_VALUE, s -> false);
        assertThat(result, is(false));
    }
}