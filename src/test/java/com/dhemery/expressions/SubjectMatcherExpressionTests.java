package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubjectMatcherExpressionTests {

    @Nested
    class AssertThat {
        @Test
        void returnsWithoutThrowing_ifMatcherAcceptsSubject() {
            Expressions.assertThat("subject", anything());
        }

        @Test
        void throwsAssertionError_ifMatcherRejectsSubject() {
            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> Expressions.assertThat("subject", not(anything()))
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of("subject", not(anything()))));
        }
    }

    @Nested
    class SatisfiedThat {
        @Test
        void returnsTrue_ifMatcherAcceptsSubject() {
            boolean result = Expressions.satisfiedThat("subject", anything());

            assertThat(result, is(true));
        }

        @Test
        void returnsFalse_ifMatcherRejectsSubject() {
            boolean result = Expressions.satisfiedThat("subject", not(anything()));

            assertThat(result, is(false));
        }
    }
}
