package com.dhemery.express;

import com.dhemery.express.helpers.Throwables;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class SubjectFunctionPredicateExpressionTests {
    private static final String SUBJECT = "subject";
    private static final SelfDescribingFunction<String, String> FUNCTION = Named.function("function", String::toUpperCase);
    private static final SelfDescribingPredicate<String> ANY_VALUE = Named.predicate("any value", t -> true);

    public static class AssertThat {
        @Test
        public void returnsWithoutThrowing_ifPredicateAcceptsFunctionOfSubject() {
            Expressions.assertThat(SUBJECT, FUNCTION, ANY_VALUE);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifPredicateRejectsFunctionOfSubject() {
            Expressions.assertThat(SUBJECT, FUNCTION, ANY_VALUE.negate());
        }

        @Test
        public void errorMessage_describesSubjectFunctionPredicateAndDerivedValue() {
            String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, FUNCTION, ANY_VALUE.negate()));

            assertThat(message, is(Diagnosis.of(SUBJECT, FUNCTION, ANY_VALUE.negate(), FUNCTION.apply(SUBJECT))));
        }
    }

    public static class SatisfiedThat {
        @Test
        public void returnsTrue_ifPredicateAcceptsFunctionOfSubject() {
            boolean result = Expressions.satisfiedThat(SUBJECT, FUNCTION, ANY_VALUE);
            assertThat(result, is(true));
        }

        @Test
        public void returnsFalse_ifPredicateRejectsFunctionOfSubject() {
            boolean result = Expressions.satisfiedThat(SUBJECT, FUNCTION, ANY_VALUE.negate());
            assertThat(result, is(false));
        }
    }
}
