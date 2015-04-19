package com.dhemery.express.expressions;

import com.dhemery.express.Condition;
import com.dhemery.express.Expressions;
import com.dhemery.express.helpers.Throwables;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.dhemery.express.helpers.Throwables.present;
import static com.dhemery.express.helpers.Throwables.throwableThrownByRunning;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class AssertThatTests {
    @Test
    public void returnsWithoutThrowingIfTheConditionIsSatisfied() {
        Expressions.assertThat(() -> true);
    }

    @Test
    public void throwsADescriptiveAssertionErrorIfTheConditionIsNotSatisfied() {
        Condition condition = () -> false;
        Runnable expression = () -> Expressions.assertThat(condition);

        Optional<Throwable> thrown = throwableThrownByRunning(expression);

        assertThat(thrown, is(present()));
        assertThat(thrown.get(), instanceOf(AssertionError.class));
        assertThat(thrown.get().getMessage(), is("Expected: " + condition));
    }

    @Test
    public void returnsWithoutThrowingIfTheSubjectMatchesThePredicate() {
        Expressions.assertThat("", String::isEmpty);
    }

    @Test
    public void throwsADescriptiveAssertionErrorIfTheSubjectMismatchesThePredicate() {
        Predicate<String> predicate = String::isEmpty;
        String subject = "foo";
        Runnable expression = () -> Expressions.assertThat(subject, predicate);

        Optional<Throwable> thrown = throwableThrownByRunning(expression);

        assertThat(thrown, is(present()));
        assertThat(thrown.get(), instanceOf(AssertionError.class));
        assertThat(thrown.get().getMessage(), is("Expected: " + subject + " " + predicate));
    }

    @Test
    public void returnsWithoutThrowingIfTheFunctionOfTheSubjectMatchesTheMatcher() {
        Function<? super String, String> toUpperCase = String::toUpperCase;
        Matcher<String> isFOO = is("FOO");
        Expressions.assertThat("foo", toUpperCase, isFOO);
    }

    @Test
    public void throwsADescriptiveAssertionErrorIfTheFunctionOfTheSubjectMismatchesTheMatcher() {
        String subject = "foo";
        Function<String, String> function = Function.identity();
        Matcher<String> matcher = is("bar");
        Runnable expression = () -> Expressions.assertThat(subject, function, matcher);

        Description expectedDiagnosis = new StringDescription();
        matcher.describeMismatch(function.apply(subject), expectedDiagnosis);
        String expectedMessage = "Expected: " + subject + " " + function + " " + matcher + "\n     but: " + expectedDiagnosis;

        Optional<Throwable> thrown = throwableThrownByRunning(expression);

        assertThat(thrown, is(present()));
        assertThat(thrown.get(), instanceOf(AssertionError.class));
        assertThat(thrown.get().getMessage(), is(expectedMessage));
    }

}
