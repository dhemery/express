package com.dhemery.express.expressions;

import com.dhemery.express.Expressions;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SatisfiedThatTests {
    @Test
    public void returnsTrueIfTheConditionIsSatisfied() {
        boolean result = Expressions.satisfiedThat(() -> true);
        assertThat(result, is(true));
    }

    @Test
    public void returnsFalseIfTheConditionIsNotSatisfied() {
        boolean result = Expressions.satisfiedThat(() -> false);
        assertThat(result, is(false));
    }

    @Test
    public void returnsTrueIfTheSubjectMatchesThePredicate() {
        boolean result = Expressions.satisfiedThat("", String::isEmpty);
        assertThat(result, is(true));
    }

    @Test
    public void returnsFalseIfTheSubjectMismatchesThePredicate() {
        boolean result = Expressions.satisfiedThat("foo", String::isEmpty);
        assertThat(result, is(false));
    }

    @Test
    public void returnsTrueIfTheMatcherMatchesTheSubject() {
        boolean result = Expressions.satisfiedThat("foo", is("foo"));
        assertThat(result, is(true));
    }

    @Test
    public void returnsFalseIfTheMatcherMismatchesTheSubject() {
        boolean result = Expressions.satisfiedThat("foo", is("food"));
        assertThat(result, is(false));
    }

    @Test
    public void returnsTrueIfTheFunctionOfTheSubjectMatchesTheMatcher() {
        boolean result = Expressions.satisfiedThat("foo", String::toUpperCase, is("FOO"));
        assertThat(result, is(true));
    }

    @Test
    public void returnsFalseIfTheFunctionOfTheSubjectMismatchesTheMatcher() {
        boolean result = Expressions.satisfiedThat("foo", String::toUpperCase, is("foo"));
        assertThat(result, is(false));
    }

    @Test
    public void returnsTrueIfTheFunctionOfTheSubjectMatchesThePredicate() {
        boolean result = Expressions.satisfiedThat("foo", String::toUpperCase, is("FOO"));
        assertThat(result, is(true));
    }

    @Test
    public void returnsFalseIfTheFunctionOfTheSubjectMismatchesThePredicate() {
        boolean result = Expressions.satisfiedThat("foo", String::toUpperCase, is("foo"));
        assertThat(result, is(false));
    }
}
