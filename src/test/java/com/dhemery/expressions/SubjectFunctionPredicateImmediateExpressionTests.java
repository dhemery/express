package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Expressions.assertThat")
class SubjectFunctionPredicateImmediateExpressionTests {
    private static final Object NON_NULL_OBJECT = new Object();
    private static final Predicate<Object> IS_NULL = Objects::isNull;
    private static final Predicate<Object> IS_NON_NULL = Objects::nonNull;
    private static final Function<Object, Object> IDENTITY = Function.identity();

    @Nested
    class AssertThat {
        @Test
        void returnsIfPredicateAcceptsFunctionOfSubject() {
            Expressions.assertThat(NON_NULL_OBJECT, IDENTITY, IS_NON_NULL);
        }

        @Test
        void throwsDiagnosticAssertionErrorIfPredicateRejectsFunctionOfSubject() {
            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> Expressions.assertThat(NON_NULL_OBJECT, IDENTITY, IS_NULL)
            );
            assertEquals(Diagnosis.of(NON_NULL_OBJECT, IDENTITY, IS_NULL, IDENTITY.apply(NON_NULL_OBJECT)), thrown.getMessage());
        }

    }
    @Nested
    class SatisfiedThat {

        @Test
        void returnsTrueIfPredicateAcceptsFunctionOfSubject() {
            assertTrue(Expressions.satisfiedThat(NON_NULL_OBJECT, IDENTITY, IS_NON_NULL));
        }

        @Test
        void returnsFalseIfPredicateRejectsFunctionOfSubject() {
            assertFalse(Expressions.satisfiedThat(NON_NULL_OBJECT, IDENTITY, IS_NULL));
        }
    }

}
