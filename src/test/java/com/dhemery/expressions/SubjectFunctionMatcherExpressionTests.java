package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import com.dhemery.expressions.helpers.Throwables;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static java.util.function.Function.identity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubjectFunctionMatcherExpressionTests {

    @Nested
    public class AssertThat {
        Function<String, String> function = Named.function("function", String::toUpperCase);

        @Test
        public void returnsWithoutThrowing_ifMatcherAcceptsFunctionOfSubject() {
            Expressions.assertThat("subject", function, is(anything()));
        }

        @Test
        public void throwsAssertionError_ifMatcherRejectsFunctionOfSubject() {
            assertThrows(
                    AssertionError.class, () ->
                    Expressions.assertThat("subject", function, not(anything())));
        }

        @Test
        public void errorMessage_describesSubjectFunctionMatcherAndMismatch() {
            String message = Throwables.messageThrownBy(() -> Expressions.assertThat("subject", function, not(anything())));

            assertThat(message, is(Diagnosis.of("subject", function, not(anything()), function.apply("subject"))));
        }
    }

    @Nested
    public class SatisfiedThat {
        @Test
        public void returnsTrue_ifMatcherAcceptsFunctionOfSubject() {
            boolean result = Expressions.satisfiedThat("subject", identity(), is(anything()));

            assertThat(result, is(true));
        }

        @Test
        public void returnsFalse_ifMatcherRejectsFunctionOfSubject() {
            boolean result = Expressions.satisfiedThat("subject", identity(), not(anything()));

            assertThat(result, is(false));
        }
    }
}
