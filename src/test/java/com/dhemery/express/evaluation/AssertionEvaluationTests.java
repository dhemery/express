package com.dhemery.express.evaluation;

import com.dhemery.express.Expressions;
import com.dhemery.express.Named;
import com.dhemery.express.SelfDescribingBooleanSupplier;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class AssertionEvaluationTests {
    @Test
    public void returnsWithoutThrowingIfTheSupplierReturnsTrue() {
        SelfDescribingBooleanSupplier supplier = Named.booleanSupplier("supplier", () -> true);
        Expressions.assertThat(supplier);
    }

    @Test
    public void throwsAssertionErrorIfTheSupplierReturnsFalse() {
        SelfDescribingBooleanSupplier supplier = Named.booleanSupplier("supplier", () -> false);

        try {
            Expressions.assertThat(supplier);
        } catch (AssertionError expected) {
            return;
        }
        Assert.fail("Expected the assertion to throw AssertionError");
    }

    @Test
    public void returnsWithoutThrowingIfThePredicateAcceptsTheSubject() {
        Expressions.assertThat("", Named.predicate("is empty", String::isEmpty));
    }

    @Test
    public void throwsAssertionErrorIfThePredicateRejectsTheSubject() {
        try {
            Expressions.assertThat("not empty", Named.predicate("is empty", String::isEmpty));
        } catch (AssertionError expected) {
            return;
        }
        Assert.fail("Expected the assertion to throw AssertionError");
    }


    @Test
    public void returnsWithoutThrowingIfTheMatcherAcceptsTheSubject() {
        Expressions.assertThat("foo", is("foo"));
    }

    @Test
    public void throwsAssertionErrorIfTheMatcherRejectsTheSubject() {
        try {
            Expressions.assertThat("foo", is("bar"));
        } catch (AssertionError expected) {
            return;
        }
        Assert.fail("Expected the assertion to throw AssertionError");
    }

    @Test
    public void returnsWithoutThrowingIfTheMatcherAcceptsTheFunctionOfTheSubject() {
        Expressions.assertThat("foo", Named.function("to upper case", String::toUpperCase), is("FOO"));
    }

    @Test
    public void throwsAssertionErrorIfTheMatcherRejectsTheFunctionOfTheSubject() {
        try {
            Expressions.assertThat("foo", Named.function("to upper case", String::toUpperCase), is("bar"));
        } catch (AssertionError expected) {
            return;
        }
        Assert.fail("Expected the assertion to throw AssertionError");
    }

    @Test
    public void returnsWithoutThrowingIfPredicateAcceptsTheFunctionOfTheSubject() {
        Expressions.assertThat("foo", Named.function("to upper case", String::toUpperCase), Named.predicate("is \"FOO\"", "FOO"::equals));
    }

    @Test
    public void throwsAssertionErrorIfThePredicateRejectsTheFunctionOfTheSubject() {
        try {
            Expressions.assertThat("foo", Named.function("to upper case", String::toUpperCase), Named.predicate("is \"foo\"", "foo"::equals));
        } catch (AssertionError expected) {
            return;
        }
        Assert.fail("Expected the assertion to throw AssertionError");
    }
}
