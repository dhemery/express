package com.dhemery.express;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NamedFunctionTests {
    private static final String IGNORED_DESCRIPTION = null;

    @Test
    public void apply_delegatesToTheUnderlyingConsumer() {
        Function<String,String> function = new NamedFunction<>(IGNORED_DESCRIPTION, String::toUpperCase);
        assertThat(function.apply("foo"), is("FOO"));
    }

    @Test
    public void describesItselfWithTheGivenName() {
        Function<String,Integer> length = new NamedFunction<>("length", String::length);
        assertThat(length.toString(), is("length"));
    }

    @Test
    public void andThen_yieldsAConsumerThatAppliesAnotherFunctionToTheResultOfThisFunction() {
        Function<String,Integer> length = new NamedFunction<>(IGNORED_DESCRIPTION, String::length);
        Function<Integer,Integer> negated = new NamedFunction<>(IGNORED_DESCRIPTION, i -> -i);
        Assert.assertThat(length.andThen(negated).apply("foo"), is(-3));
    }

    @Test
    public void andThen_yieldsAConsumerNamedToDescribeItsComposition() {
        Function<String,Integer> length = new NamedFunction<>("length", String::length);
        Function<Integer,Integer> negation = new NamedFunction<>("negation", i -> -i);
        Assert.assertThat(length.andThen(negation).toString(), is("(negation of length)"));
    }

    @Test
    public void compose_yieldsAConsumerThatAppliesThisFunctionToTheResultOfAnotherFunction() {
        Function<String,Integer> length = new NamedFunction<>(IGNORED_DESCRIPTION, String::length);
        Function<Integer,Integer> negation = new NamedFunction<>(IGNORED_DESCRIPTION, i -> -i);
        Assert.assertThat(negation.compose(length).apply("foo"), is(-3));
    }

    @Test
    public void compose_yieldsAConsumerNamedToDescribeItsComposition() {
        Function<String,Integer> length = new NamedFunction<>("length", String::length);
        Function<Integer,Integer> negation = new NamedFunction<>("negation", i -> -i);
        Assert.assertThat(negation.compose(length).toString(), is("(negation of length)"));
    }
}
