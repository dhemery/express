package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.helpers.Throwables;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Enclosed.class)
public class SubjectPredicateExpressionTests {
    private static final SelfDescribingPredicate<Object> ANY_VALUE = Named.predicate("any value", t -> true);
    public static final String SUBJECT = "subject";

    public static class AssertThat {
        @Test
        public void returnsWithoutThrowing_ifMatcherAcceptsSubject() {
            Expressions.assertThat(SUBJECT, ANY_VALUE);
        }

        @Test(expected = AssertionError.class)
        public void throwsAssertionError_ifMatcherRejectsSubject() {
            Expressions.assertThat(SUBJECT, ANY_VALUE.negate());
        }

        @Test
        public void errorMessage_describesSubjectAndPredicate() {
            String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, ANY_VALUE.negate()));

            assertThat(message, is(Diagnosis.of(SUBJECT, ANY_VALUE.negate())));
        }
    }

    public static class SatisfiedThat {
        @Test
        public void returnsTrue_ifPredicateAcceptsSubject() {
            boolean result = Expressions.satisfiedThat(SUBJECT, ANY_VALUE);
            assertThat(result, is(true));
        }

        @Test
        public void returnsFalse_ifPredicateRejectsSubject() {
            boolean result = Expressions.satisfiedThat(SUBJECT, ANY_VALUE.negate());
            assertThat(result, is(false));
        }
    }
}
