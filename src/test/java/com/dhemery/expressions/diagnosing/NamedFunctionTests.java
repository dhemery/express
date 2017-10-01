package com.dhemery.expressions.diagnosing;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static java.lang.String.format;
import static java.util.function.Function.identity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class NamedFunctionTests {
    @Test
    void apply_delegatesToTheUnderlyingConsumer() {
        Function<String, String> function = new NamedFunction<>("", String::toUpperCase);
        assertThat(function.apply("foo"), is("FOO"));
    }

    @Test
    void describesItselfWithTheGivenName() {
        Function<String, String> function = new NamedFunction<>("function", identity());

        assertThat(String.valueOf(function), is("function"));
    }

    @Test
    void andThen_yieldsAConsumerThatAppliesAnotherFunctionToTheResultOfTheUnderlyingFunction() {
        Function<String, Integer> length = new NamedFunction<>("", String::length);
        Function<Integer, Integer> negated = new NamedFunction<>("", i -> -i);

        assertThat(length.andThen(negated).apply("foo"), is(-3));
    }

    @Test
    void andThen_yieldsAConsumerThatDescribesItsComposition() {
        Function<Object, Object> before = new NamedFunction<>("before", identity());
        Function<Object, Object> after = identity();

        Function<Object, Object> composed = before.andThen(after);

        assertThat(String.valueOf(composed), is(format("(%s of before)", after)));
    }

    @Test
    void compose_yieldsAFunctionThatAppliesTheUnderlyingFunctionToTheResultOfAnotherFunction() {
        Function<String, Integer> length = new NamedFunction<>("", String::length);
        Function<Integer, Integer> negation = new NamedFunction<>("", i -> -i);

        assertThat(negation.compose(length).apply("foo"), is(-3));
    }

    @Test
    void compose_yieldsAFunctionThatDescribesItsComposition() {
        Function<Object, Object> before = identity();
        Function<Object, Object> after = new NamedFunction<>("after", identity());

        Function<Object, Object> composed = after.compose(before);

        assertThat(String.valueOf(composed), is(format("(after of %s)", before)));
    }
}
