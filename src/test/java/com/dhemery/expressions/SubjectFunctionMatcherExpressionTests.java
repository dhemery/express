package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static java.util.function.Function.identity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubjectFunctionMatcherExpressionTests {

    @Nested
    class AssertThat {
        Function<String, String> function = Named.function("function", String::toUpperCase);

        @Test
        void returnsWithoutThrowing_ifMatcherAcceptsFunctionOfSubject() {
            Expressions.assertThat("subject", function, is(anything()));
        }

        @Test
        void throwsAssertionError_ifMatcherRejectsFunctionOfSubject() {
            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> Expressions.assertThat("subject", function, not(anything()))
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of("subject", function, not(anything()), function.apply("subject"))));
        }
    }

    @Nested
    class SatisfiedThat {
        @Test
        void returnsTrue_ifMatcherAcceptsFunctionOfSubject() {
            boolean result = Expressions.satisfiedThat("subject", identity(), is(anything()));

            assertThat(result, is(true));
        }

        @Test
        void returnsFalse_ifMatcherRejectsFunctionOfSubject() {
            boolean result = Expressions.satisfiedThat("subject", identity(), not(anything()));

            assertThat(result, is(false));
        }
    }
}
