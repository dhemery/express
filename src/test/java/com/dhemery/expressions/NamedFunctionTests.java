package com.dhemery.expressions;

import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

import static java.lang.String.format;
import static java.util.function.Function.identity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NamedFunctionTests {
    @Test
    public void apply_delegatesToTheUnderlyingConsumer() {
        Function<String, String> function = new NamedFunction<>("", String::toUpperCase);
        assertThat(function.apply("foo"), is("FOO"));
    }

    @Test
    public void describesItselfWithTheGivenName() {
        SelfDescribingFunction<String, String> function = new NamedFunction<>("function", identity());

        assertThat(StringDescription.toString(function), is("function"));
        assertThat(String.valueOf(function), is("function"));
    }

    @Test
    public void andThen_yieldsAConsumerThatAppliesAnotherFunctionToTheResultOfTheUnderlyingFunction() {
        Function<String, Integer> length = new NamedFunction<>("", String::length);
        Function<Integer, Integer> negated = new NamedFunction<>("", i -> -i);

        Assert.assertThat(length.andThen(negated).apply("foo"), is(-3));
    }

    @Test
    public void andThen_yieldsAConsumerThatDescribesItsComposition() {
        SelfDescribingFunction<Object, Object> before = new NamedFunction<>("before", identity());
        Function<Object, Object> after = identity();

        SelfDescribingFunction<Object, Object> composed = before.andThen(after);

        Assert.assertThat(String.valueOf(composed), is(format("(%s of before)", BestDescription.of(after))));
        Assert.assertThat(StringDescription.toString(composed), is(format("(%s of before)", BestDescription.of(after))));
    }

    @Test
    public void compose_yieldsAFunctionThatAppliesTheUnderlyingFunctionToTheResultOfAnotherFunction() {
        Function<String, Integer> length = new NamedFunction<>("", String::length);
        Function<Integer, Integer> negation = new NamedFunction<>("", i -> -i);

        Assert.assertThat(negation.compose(length).apply("foo"), is(-3));
    }

    @Test
    public void compose_yieldsAFunctionThatDescribesItsComposition() {
        Function<Object, Object> before = identity();
        SelfDescribingFunction<Object, Object> after = new NamedFunction<>("after", identity());

        SelfDescribingFunction<Object, Object> composed = after.compose(before);

        Assert.assertThat(String.valueOf(composed), is(format("(after of %s)", BestDescription.of(before))));
        Assert.assertThat(StringDescription.toString(composed), is(format("(after of %s)", BestDescription.of(before))));
    }
}
