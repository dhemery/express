package com.dhemery.express;

import com.dhemery.express.helpers.Throwables;
import org.jmock.Expectations;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SubjectFunctionPredicateExpressionsTests {
    private static final String SUBJECT = "subject";

    SelfDescribingFunction<String, String> function = Named.function("function", String::toUpperCase);
    SelfDescribingPredicate<String> anyValue = Named.predicate("any value", t -> true);

    @Test
    public void assertThat_returnsWithoutThrowing_ifPredicateAcceptsFunctionOfSubject() {
        Expressions.assertThat(SUBJECT, function, anyValue);
    }

    @Test(expected = AssertionError.class)
    public void assertThat_throwsAssertionError_ifPredicateRejectsFunctionOfSubject() {
        Expressions.assertThat(SUBJECT, function, anyValue.negate());
    }

    @Test
    public void assertThat_errorMessage_describesSubjectFunctionPredicateDerivedValue() {
        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, function, anyValue.negate()));

        assertThat(message, is(Diagnosis.of(SUBJECT, function, anyValue.negate(), function.apply(SUBJECT))));
    }

    @Test
    public void satisfiedThat_returnsTrue_ifPredicateAcceptsFunctionOfSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, function, anyValue);
        assertThat(result, is(true));
    }

    @Test
    public void satisfiedThat_returnsFalse_ifPredicateRejectsFunctionOfSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, function, anyValue.negate());
        assertThat(result, is(false));
    }
}
