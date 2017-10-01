package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubjectFunctionPredicateExpressionTests {
    private static final String SUBJECT = "subject";
    private static final Function<String, String> FUNCTION = Named.function("function", String::toUpperCase);
    private static final Predicate<String> ANY_VALUE = Named.predicate("any value", t -> true);

    @Nested
    public static class AssertThat {
        @Test
        public void returnsWithoutThrowing_ifPredicateAcceptsFunctionOfSubject() {
            Expressions.assertThat(SUBJECT, FUNCTION, ANY_VALUE);
        }

        @Test
        public void throwsAssertionError_ifPredicateRejectsFunctionOfSubject() {
            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> Expressions.assertThat(SUBJECT, FUNCTION, ANY_VALUE.negate())
            );
            assertThat(thrown.getMessage(), is(Diagnosis.of(SUBJECT, FUNCTION, ANY_VALUE.negate(), FUNCTION.apply(SUBJECT))));
        }
    }

    @Nested
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
