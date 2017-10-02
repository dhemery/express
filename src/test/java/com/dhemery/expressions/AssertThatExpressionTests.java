package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Expressions.assertThat")
class AssertThatExpressionTests {

    @Test
    void returnsIfSupplierSuppliesTrue() {
        Expressions.assertThat(() -> true);
    }

    @Test
    void throwsDiagnosticAssertionErrorIfSupplierSuppliesFalse() {
        BooleanSupplier unsatisfiedCondition = () -> false;

        AssertionError thrown = assertThrows(
                AssertionError.class,
                () -> Expressions.assertThat(unsatisfiedCondition)
        );

        assertEquals(Diagnosis.of(unsatisfiedCondition), thrown.getMessage());
    }

    @Test
    void returnsIfPredicateAcceptsSubject() {
        Expressions.assertThat("foo", s -> true);
    }

    @Test
    void throwsDiagnosticAssertionErrorIfPredicateRejectsSubject() {
        String subject = "subject";
        Predicate<String> rejected = s -> false;

        AssertionError thrown = assertThrows(
                AssertionError.class,
                () -> Expressions.assertThat(subject, rejected)
        );

        assertEquals(Diagnosis.of(subject, rejected), thrown.getMessage());
    }
}
