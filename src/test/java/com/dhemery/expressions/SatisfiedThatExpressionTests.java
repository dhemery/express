package com.dhemery.expressions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Expressions.satisfiedThat")
class SatisfiedThatExpressionTests {
    private static final Object NON_NULL_OBJECT = new Object();
    private static final Predicate<Object> IS_NON_NULL = Objects::nonNull;
    private static final Predicate<Object> IS_NULL = Objects::isNull;
    private static final Function<Object, Object> IDENTITY = Function.identity();

    @Test
    void returnsTrueIfSupplierSuppliesTrue() {
        assertTrue(Expressions.satisfiedThat(() -> true));
    }

    @Test
    void returnsFalseIfSupplierSuppliesFalse() {
        assertFalse(Expressions.satisfiedThat(() -> false));
    }

    @Test
    void returnsTrueIfPredicateAcceptsSubject() {
        assertTrue(Expressions.satisfiedThat(NON_NULL_OBJECT, IS_NON_NULL));
    }

    @Test
    void returnsFalseIfPredicateRejectsSubject() {
        assertFalse(Expressions.satisfiedThat(NON_NULL_OBJECT, IS_NULL));
    }

    @Test
    void returnsTrueIfPredicateAcceptsFunctionOfSubject() {
        assertTrue(Expressions.satisfiedThat(NON_NULL_OBJECT, IDENTITY, IS_NON_NULL));
    }

    @Test
    void returnsFalseIfPredicateRejectsFunctionOfSubject() {
        assertFalse(Expressions.satisfiedThat(NON_NULL_OBJECT, IDENTITY, IS_NULL));
    }
}
