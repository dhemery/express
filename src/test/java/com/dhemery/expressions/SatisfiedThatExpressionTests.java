package com.dhemery.expressions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Expressions.satisfiedThat")
class SatisfiedThatExpressionTests {

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
        assertTrue(Expressions.satisfiedThat("subject", s -> true));
    }

    @Test
    void returnsFalseIfPredicateRejectsSubject() {
        assertFalse(Expressions.satisfiedThat("subject", s -> false));
    }
}
