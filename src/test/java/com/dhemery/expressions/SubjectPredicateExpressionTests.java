package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubjectPredicateExpressionTests {
    private static final Predicate<Object> ANY_VALUE = Named.predicate("any value", t -> true);
    private static final String SUBJECT = "subject";

    @Nested
    class AssertThat {
        @Test
        void returnsWithoutThrowing_ifMatcherAcceptsSubject() {
            Expressions.assertThat(SUBJECT, ANY_VALUE);
        }

        @Test
        void throwsAssertionError_ifMatcherRejectsSubject() {
            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> Expressions.assertThat(SUBJECT, ANY_VALUE.negate())
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(SUBJECT, ANY_VALUE.negate())));
        }
    }

    @Nested
    class SatisfiedThat {
        @Test
        void returnsTrue_ifPredicateAcceptsSubject() {
            boolean result = Expressions.satisfiedThat(SUBJECT, ANY_VALUE);
            assertThat(result, is(true));
        }

        @Test
        void returnsFalse_ifPredicateRejectsSubject() {
            boolean result = Expressions.satisfiedThat(SUBJECT, ANY_VALUE.negate());
            assertThat(result, is(false));
        }
    }
}
