package com.dhemery.express.expressions;

import com.dhemery.express.Condition;
import com.dhemery.express.Expressions;
import com.dhemery.express.Named;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SatisfiedThatTests {
    @Test
    public void returnsTrueIfTheConditionIsSatisfied() {
        Condition alwaysSatisfied = Named.condition("always satisfied", () -> true);
        boolean result = Expressions.satisfiedThat(alwaysSatisfied);
        assertThat(result, is(true));
    }

    @Test
    public void returnsFalseIfTheConditionIsNotSatisfied() {
        Condition neverSatisfied = Named.condition("never satisfied", () -> false);
        boolean result = Expressions.satisfiedThat(neverSatisfied);
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
