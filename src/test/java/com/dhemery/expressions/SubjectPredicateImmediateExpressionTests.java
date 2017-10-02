package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class SubjectPredicateImmediateExpressionTests {
    private static final Object NON_NULL_OBJECT = new Object();
    private static final Predicate<Object> IS_NULL = Objects::isNull;
    private static final Predicate<Object> IS_NON_NULL = Objects::nonNull;

    @Nested
    class AssertThat {
        @Test
        void returnsIfPredicateAcceptsSubject() {
            Expressions.assertThat(NON_NULL_OBJECT, IS_NON_NULL);
        }

        @Test
        void throwsDiagnosticAssertionErrorIfPredicateRejectsSubject() {

            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> Expressions.assertThat(NON_NULL_OBJECT, IS_NULL)
            );

            assertEquals(Diagnosis.of(NON_NULL_OBJECT, IS_NULL), thrown.getMessage());
        }
    }

    @Nested
    class SatisfiedThat {

        @Test
        void returnsTrueIfPredicateAcceptsSubject() {
            assertTrue(Expressions.satisfiedThat(NON_NULL_OBJECT, IS_NON_NULL));
        }

        @Test
        void returnsFalseIfPredicateRejectsSubject() {
            assertFalse(Expressions.satisfiedThat(NON_NULL_OBJECT, IS_NULL));
        }
    }
}
