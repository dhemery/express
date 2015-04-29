package com.dhemery.express.diagnosis;

import com.dhemery.express.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.dhemery.express.helpers.Throwables.present;
import static com.dhemery.express.helpers.Throwables.throwableThrownByRunning;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AssertionDiagnosisTests {
    @Test
    public void assertionMessageDescribesSupplier() {
        SelfDescribingBooleanSupplier supplier = Named.booleanSupplier("supplier name", () -> false);

        String[] expectedLines = new String[]{
                "",
                "Expected: " + StringDescription.toString(supplier)
        };

        String[] lines = messageOfAssertionErrorThrownBy(() -> Expressions.assertThat(supplier));

        assertThat(lines, is(arrayContaining(expectedLines)));
    }

    @Test
    public void assertionMessageDescribesPredicateAndSubject() {
        SelfDescribingPredicate<String> predicate = Named.predicate("an empty string", String::isEmpty);
        String subject = "subject";

        String[] expectedLines = new String[]{
                "",
                "Expected: " + StringDescription.toString(predicate),
                "     but: was " + new StringDescription().appendValue(subject)
        };

        String[] lines = messageOfAssertionErrorThrownBy(() -> Expressions.assertThat(subject, predicate));

        assertThat(lines, is(arrayContaining(expectedLines)));
    }

    @Test
    public void assertionMessageDescribesMatcherAndMismatchOfSubject() {
        String subject = "subject";
        Matcher<String> matcher = is("foo");

        Description mismatchDescription = new StringDescription();
        matcher.describeMismatch(subject, mismatchDescription);
        String[] expectedLines = new String[]{
                "",
                "Expected: " + StringDescription.toString(matcher),
                "     but: " + mismatchDescription
        };

        String[] lines = messageOfAssertionErrorThrownBy(() -> Expressions.assertThat(subject, matcher));

        assertThat(lines, is(arrayContaining(expectedLines)));
    }

    @Test
    public void assertionMessageDescribesSubjectAndMatcherAndFunctionAndMismatchOfFunctionResult() {
        String subject = "subject";
        SelfDescribingFunction<String, String> function = Named.function("upper case", String::toUpperCase);
        Matcher<String> matcher = is("bar");

        String functionValue = function.apply(subject);
        Description mismatchDescription = new StringDescription();
        matcher.describeMismatch(functionValue, mismatchDescription);

        String[] expectedLines = new String[]{
                subject,
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(matcher),
                "     but: " + mismatchDescription
        };

        String[] lines = messageOfAssertionErrorThrownBy(() -> Expressions.assertThat(subject, function, matcher));
        assertThat(lines, is(arrayContaining(expectedLines)));
    }

    @Test
    public void assertionMessageDescribesSubjectAndPredicateAndFunctionAndFunctionResult() {
        String subject = "subject";
        SelfDescribingFunction<String, String> function = Named.function("upper case", String::toUpperCase);
        SelfDescribingPredicate<String> predicate = Named.predicate("equals \"bar\"", "bar"::equals);

        String functionValue = function.apply(subject);

        String[] expectedLines = new String[]{
                subject,
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(predicate),
                "     but: was " + new StringDescription().appendValue(functionValue)
        };

        String[] lines = messageOfAssertionErrorThrownBy(() -> Expressions.assertThat(subject, function, predicate));
        assertThat(lines, is(arrayContaining(expectedLines)));
    }

    public String[] messageOfAssertionErrorThrownBy(Runnable runnable) {
        Throwable thrown = assertionErrorThrownBy(runnable);
        String message = thrown.getMessage();
        return message.split(System.lineSeparator());
    }

    private Throwable assertionErrorThrownBy(Runnable expression) {
        try {
            expression.run();
        } catch (AssertionError thrown) {
            return thrown;
        }
        throw new AssertionError("The assertion did not throw an AssertionError");
    }
}
