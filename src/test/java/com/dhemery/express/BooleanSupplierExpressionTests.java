package com.dhemery.express;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.dhemery.express.helpers.Throwables.messageThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class BooleanSupplierExpressionTests {

    public static class AssertThat {
        @Test
        public void returnsWithoutThrowing_ifSupplierReturnsTrue() {
            SelfDescribingBooleanSupplier supplier = Named.booleanSupplier("", () -> true);

            Expressions.assertThat(supplier);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifSupplierReturnsFalse() {
            SelfDescribingBooleanSupplier supplier = Named.booleanSupplier("", () -> false);

            Expressions.assertThat(supplier);
        }

        @Test
        public void errorMessage_describesSupplier() {
            SelfDescribingBooleanSupplier supplier = Named.booleanSupplier("supplier", () -> false);

            String message = messageThrownBy(() -> Expressions.assertThat(supplier));

            assertThat(message, is(Diagnosis.of(supplier)));
        }
    }

    public static class SatisfiedThat {
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
