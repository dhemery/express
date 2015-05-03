package com.dhemery.express;

import com.dhemery.express.helpers.Throwables;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.function.Predicate;

import static com.dhemery.express.helpers.Actions.appendItsStringValue;
import static com.dhemery.express.helpers.Actions.appendTheMismatchDescriptionOfTheItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SubjectPredicateExpressionTests {
    private static final String SUBJECT = "subject";
    SelfDescribingPredicate<Object> anyValue = Named.predicate("any value", t -> true);

    @Test
    public void assertThat_returnsWithoutThrowing_ifMatcherAcceptsSubject() {
        Expressions.assertThat(SUBJECT, anyValue);
    }

    @Test(expected = AssertionError.class)
    public void assertThat_throwsAssertionError_ifMatcherRejectsSubject() {
        Expressions.assertThat(SUBJECT, anyValue.negate());
    }

    @Test
    public void assertThat_errorMessageIncludesDiagnosis() {
        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, anyValue.negate()));

        assertThat(message, is(Diagnosis.of(SUBJECT, anyValue.negate())));
    }

    @Test
    public void satisfiedThat_returnsTrue_ifPredicateAcceptsSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, anyValue);
        assertThat(result, is(true));
    }

    @Test
    public void satisfiedThat_returnsFalse_ifPredicateRejectsSubject() {
        boolean result = Expressions.satisfiedThat(SUBJECT, anyValue.negate());
        assertThat(result, is(false));
    }
}
