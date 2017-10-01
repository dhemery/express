package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BooleanSupplierExpressionTests {

    @Nested
    public class AssertThat {
        @Test
        public void returnsWithoutThrowing_ifSupplierReturnsTrue() {
            System.out.println(System.getProperty("java.version"));
            BooleanSupplier supplier = Named.booleanSupplier("", () -> true);

            Expressions.assertThat(supplier);
        }

        @Test
        public void throwsAssertionError_ifSupplierReturnsFalse() {
            BooleanSupplier supplier = Named.booleanSupplier("supplier", () -> false);

            AssertionError thrown = assertThrows(
                    AssertionError.class,
                    () -> Expressions.assertThat(supplier)
            );

            assertThat(thrown.getMessage(), is(Diagnosis.of(supplier)));
        }
    }

    @Nested
    public class SatisfiedThat {
        @Test
        public void returnsTrue_ifSupplierReturnsTrue() {
            assertThat(Expressions.satisfiedThat(() -> true), is(true));
        }

        @Test
        public void returnsFalse_ifSupplierReturnsFalse() {
            assertThat(Expressions.satisfiedThat(() -> false), is(false));
        }
    }

}
