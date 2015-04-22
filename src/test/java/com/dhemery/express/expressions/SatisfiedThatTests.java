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
    public void returnsTrueIfThePredicateAcceptsTheSubject() {
        boolean result = Expressions.satisfiedThat("", String::isEmpty);
        assertThat(result, is(true));
    }

    @Test
    public void returnsFalseIfThePredicateRejectsTheSubject() {
        boolean result = Expressions.satisfiedThat("foo", String::isEmpty);
        assertThat(result, is(false));
    }

    @Test
    public void returnsTrueIfTheMatcherAcceptsTheSubject() {
        boolean result = Expressions.satisfiedThat("foo", is("foo"));
        assertThat(result, is(true));
    }

    @Test
    public void returnsFalseIfTheMatcherRejectsTheSubject() {
        boolean result = Expressions.satisfiedThat("foo", is("food"));
        assertThat(result, is(false));
    }

    @Test
    public void returnsTrueIfTheMatcherAcceptsTheFunctionOfTheSubject() {
        boolean result = Expressions.satisfiedThat("foo", String::toUpperCase, is("FOO"));
        assertThat(result, is(true));
    }

    @Test
    public void returnsFalseIfTheMatcherRejectsTheFunctionOfTheSubject() {
        boolean result = Expressions.satisfiedThat("foo", String::toUpperCase, is("foo"));
        assertThat(result, is(false));
    }

    @Test
    public void returnsTrueIfThePredicateAcceptsTheFunctionOfTheSubject() {
        boolean result = Expressions.satisfiedThat("foo", String::toUpperCase, is("FOO"));
        assertThat(result, is(true));
    }

    @Test
    public void returnsFalseIfThePredicateRejectsTheFunctionOfTheSubject() {
        boolean result = Expressions.satisfiedThat("foo", String::toUpperCase, is("foo"));
        assertThat(result, is(false));
    }
}
