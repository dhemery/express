package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class SubjectPredicateImmediateExpressionTests {
    private static final String SUBJECT = "subject";
    private static final Predicate<String> SATISFIED_PREDICATE = Named.predicate("equal to", s -> Objects.equals(s, s));
    private static final Predicate<String> UNSATISFIED_PREDICATE = SATISFIED_PREDICATE.negate();

    @Nested
    class AssertThat {
        @Test
        void returnsIfPredicateAcceptsSubject() {
            Expressions.assertThat(SUBJECT, SATISFIED_PREDICATE);
        }

        @Test
        void throwsDiagnosticAssertionErrorIfPredicateRejectsSubject() {

            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> Expressions.assertThat(SUBJECT, UNSATISFIED_PREDICATE)
            );

            assertEquals(Diagnosis.of(SUBJECT, UNSATISFIED_PREDICATE), thrown.getMessage());
        }
    }

    @Nested
    class SatisfiedThat {

        @Test
        void returnsTrueIfPredicateAcceptsSubject() {
            assertTrue(Expressions.satisfiedThat(SUBJECT, SATISFIED_PREDICATE));
        }

        @Test
        void returnsFalseIfPredicateRejectsSubject() {
            assertFalse(Expressions.satisfiedThat(SUBJECT, UNSATISFIED_PREDICATE));
        }
    }
}
