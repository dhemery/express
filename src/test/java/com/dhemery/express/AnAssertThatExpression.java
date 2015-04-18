package com.dhemery.express;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

// TODO: Tests for descriptive assertion messages
public class AnAssertThatExpression {
    @Test
    public void returnsWithoutThrowingIfTheConditionIsSatisfied() {
        Expressions.assertThat(() -> true);
    }

    @Test
    public void throwsIfTheConditionIsNotSatisfied() {
        try {
            Expressions.assertThat(() -> false);
        } catch (AssertionError thrown) {
            return;
        }
        throw new AssertionError("Should have thrown an AssertionError");
    }

    @Test
    public void returnsWithoutThrowingIfTheSubjectMatchesThePredicate() {
        Expressions.assertThat("", String::isEmpty);
    }

    @Test
    public void throwsIfTheSubjectMismatchesThePredicate() {
        try {
            Expressions.assertThat("foo", String::isEmpty);
        } catch (AssertionError thrown) {
            return;
        }
        throw new AssertionError("Should have thrown an AssertionError");
    }

    @Test
    public void returnsWithoutThrowingIfTheMatcherMatchesTheSubject() {
            Expressions.assertThat("foo", is("foo"));
    }

    @Test
    public void throwsIfTheMatcherMismatchesTheSubject() {
        try {
            Expressions.assertThat("foo", is("food"));
        } catch (AssertionError thrown) {
            return;
        }
        throw new AssertionError("Should have thrown an AssertionError");
    }

    @Test
    public void returnsWithoutThrowingIfTheFunctionOfTheSubjectMatchesTheMatcher() {
        Function<? super String, String> toUpperCase = String::toUpperCase;
        Matcher<String> isFOO = is("FOO");
        Expressions.assertThat("foo", toUpperCase, isFOO);
    }

    @Test
    public void throwsIfTheFunctionOfTheSubjectMismatchesTheMatcher() {
        Function<String, String> toUpperCase = String::toUpperCase;
        Matcher<String> isFOO = is("foo");
        try {
            Expressions.assertThat("foo", toUpperCase, isFOO);
        } catch (AssertionError thrown) {
            return;
        }
        throw new AssertionError("Should have thrown an AssertionError");
    }
}
