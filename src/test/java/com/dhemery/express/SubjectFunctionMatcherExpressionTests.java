package com.dhemery.express;

import com.dhemery.express.helpers.Throwables;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static java.util.function.Function.identity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(Enclosed.class)
public class SubjectFunctionMatcherExpressionTests {

    public static class AssertThat {
        SelfDescribingFunction<String, String> function = Named.function("function", String::toUpperCase);

        @Test
        public void returnsWithoutThrowing_ifMatcherAcceptsFunctionOfSubject() {
            Expressions.assertThat("subject", function, is(anything()));
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifMatcherRejectsFunctionOfSubject() {
            Expressions.assertThat("subject", function, not(anything()));
        }

        @Test
        public void errorMessageDescribesSubjectFunctionMatcherMismatch() {
            String message = Throwables.messageThrownBy(() -> Expressions.assertThat("subject", function, not(anything())));

            assertThat(message, is(Diagnosis.of("subject", function, not(anything()), function.apply("subject"))));
        }
    }

    public static class SatisfiedThat {
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
