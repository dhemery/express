package com.dhemery.expressions.diagnosing;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static java.lang.String.format;
import static java.util.function.Function.identity;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NamedFunctionTests {
    @Test
    void apply_delegatesToTheUnderlyingConsumer() {
        Function<String, String> function = new NamedFunction<>("", String::toUpperCase);
        assertEquals("FOO", function.apply("foo"));
    }

    @Test
    void describesItselfWithTheGivenName() {
        Function<String, String> function = new NamedFunction<>("function", identity());

        assertEquals("function", String.valueOf(function));
    }

    @Test
    void andThen_yieldsAConsumerThatAppliesAnotherFunctionToTheResultOfTheUnderlyingFunction() {
        Function<String, Integer> length = new NamedFunction<>("", String::length);
        Function<Integer, Integer> negated = new NamedFunction<>("", i -> -i);

        assertEquals(Integer.valueOf(-3), length.andThen(negated).apply("foo"));
    }

    @Test
    void andThen_yieldsAConsumerThatDescribesItsComposition() {
        Function<Object, Object> before = new NamedFunction<>("before", identity());
        Function<Object, Object> after = identity();

        Function<Object, Object> composed = before.andThen(after);

        assertEquals(format("(%s of before)", after), String.valueOf(composed));
    }

    @Test
    void compose_yieldsAFunctionThatAppliesTheUnderlyingFunctionToTheResultOfAnotherFunction() {
        Function<String, Integer> length = new NamedFunction<>("", String::length);
        Function<Integer, Integer> negation = new NamedFunction<>("", i -> -i);

        assertEquals(Integer.valueOf(-3), negation.compose(length).apply("foo"));
    }

    @Test
    void compose_yieldsAFunctionThatDescribesItsComposition() {
        Function<Object, Object> before = identity();
        Function<Object, Object> after = new NamedFunction<>("after", identity());

        Function<Object, Object> composed = after.compose(before);

        assertEquals(format("(after of %s)", before), String.valueOf(composed));
    }
}
