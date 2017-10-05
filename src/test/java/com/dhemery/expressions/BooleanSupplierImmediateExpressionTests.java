package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

class BooleanSupplierImmediateExpressionTests {
    private static final BooleanSupplier SATISFIED_CONDITION = Named.booleanSupplier("satisfied condition", () -> true);
    private static final BooleanSupplier UNSATISFIED_CONDITION = Named.booleanSupplier("unsatisfied condition", () -> false);
    @Nested
    class AssertThat {
        @Test
        void returnsIfSupplierSuppliesTrue() {
            Expressions.assertThat(SATISFIED_CONDITION);
        }

        @Test
        void throwsDiagnosticAssertionErrorIfSupplierSuppliesFalse() {
            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> Expressions.assertThat(UNSATISFIED_CONDITION)
            );

            assertEquals(Diagnosis.of(UNSATISFIED_CONDITION), thrown.getMessage());
        }
    }

    @Nested
    class SatisfiedThat {
        @ParameterizedTest
        @CsvSource({"true", "false"})
        void returnsValueSuppliedBySupplier(boolean suppliedValue) {
            assertEquals(suppliedValue, Expressions.satisfiedThat(() -> suppliedValue));
        }
    }
}
