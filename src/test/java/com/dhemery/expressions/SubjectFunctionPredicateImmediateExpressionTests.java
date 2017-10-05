package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class SubjectFunctionPredicateImmediateExpressionTests {
    private static final String SUBJECT = "subject";
    private static final Function<String,String> FUNCTION = Named.function("plural of ", s -> s + "s");
    private static final Predicate<String> SATISFIED_PREDICATE = Named.predicate("equal to", s -> Objects.equals(s, s));
    private static final Predicate<String> UNSATISFIED_PREDICATE = SATISFIED_PREDICATE.negate();

    @Nested
    class AssertThat {
        @Test
        void returnsIfPredicateAcceptsFunctionOfSubject() {
            Expressions.assertThat(SUBJECT, FUNCTION, SATISFIED_PREDICATE);
        }

        @Test
        void throwsDiagnosticAssertionErrorIfPredicateRejectsFunctionOfSubject() {
            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> Expressions.assertThat(SUBJECT, FUNCTION, UNSATISFIED_PREDICATE)
            );
            assertEquals(Diagnosis.of(SUBJECT, FUNCTION, UNSATISFIED_PREDICATE, FUNCTION.apply(SUBJECT)), thrown.getMessage());
        }
    }

    @Nested
    class SatisfiedThat {

        @ParameterizedTest
        @CsvSource({"true", "false"})
        void returnsWhetherPredicateAcceptsFunctionOfSubject(boolean predicateReturnValue) {
            assertEquals(predicateReturnValue, Expressions.satisfiedThat(SUBJECT, FUNCTION, v -> predicateReturnValue));
        }
    }
}
