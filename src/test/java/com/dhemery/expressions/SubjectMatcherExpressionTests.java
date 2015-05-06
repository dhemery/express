package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.helpers.Throwables;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(Enclosed.class)
public class SubjectMatcherExpressionTests {

    public static class AssertThat {
        @Test
        public void returnsWithoutThrowing_ifMatcherAcceptsSubject() {
            Expressions.assertThat("subject", anything());
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifMatcherRejectsSubject() {
            Expressions.assertThat("subject", not(anything()));
        }

        @Test
        public void errorMessage_describesMatcherAndMismatch() {
            String message = Throwables.messageThrownBy(() -> Expressions.assertThat("subject", not(anything())));

            assertThat(message, is(Diagnosis.of("subject", not(anything()))));
        }
    }

    public static class SatisfiedThat {
        @Test
        public void returnsTrue_ifMatcherAcceptsSubject() {
            boolean result = Expressions.satisfiedThat("subject", anything());

            assertThat(result, is(true));
        }

        @Test
        public void returnsFalse_ifMatcherRejectsSubject() {
            boolean result = Expressions.satisfiedThat("subject", not(anything()));

            assertThat(result, is(false));
        }
    }
}
