package com.dhemery.express;

import com.dhemery.express.helpers.Throwables;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(Enclosed.class)
public class SubjectFunctionMatcherExpressionTests {
    protected static class SubjectFunctionMatcherSetup {
        static final String SUBJECT = "subject";

        SelfDescribingFunction<String, String> function = Named.function("function", String::toUpperCase);
    }

    public static class AssertThat extends SubjectFunctionMatcherSetup {

        @Test
        public void returnsWithoutThrowing_ifMatcherAcceptsFunctionOfSubject() {
            Expressions.assertThat(SUBJECT, function, is(anything()));
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifMatcherRejectsFunctionOfSubject() {
            Expressions.assertThat(SUBJECT, function, not(anything()));
        }

        @Test
        public void errorMessageDescribesSubjectFunctionMatcherMismatch() {
            String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, function, not(anything())));

            assertThat(message, is(Diagnosis.of(SUBJECT, function, not(anything()), function.apply(SUBJECT))));
        }
    }

    public static class SatisfiedThat extends SubjectFunctionMatcherSetup {
        @Test
        public void returnsTrue_ifMatcherAcceptsFunctionOfSubject() {
            boolean result = Expressions.satisfiedThat(SUBJECT, function, is(anything()));

            assertThat(result, is(true));
        }

        @Test
        public void returnsFalse_ifMatcherRejectsFunctionOfSubject() {
            boolean result = Expressions.satisfiedThat(SUBJECT, function, not(anything()));

            assertThat(result, is(false));
        }
    }
}
