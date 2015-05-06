package com.dhemery.expressions;

import org.hamcrest.SelfDescribing;

import java.util.function.Function;

/**
 * A {@link Function} that can describe itself.
 *
 * @param <T>
 *         the type of the input to the function
 * @param <R>
 *         the type of the result of the function
 */
public interface SelfDescribingFunction<T, R> extends Function<T, R>, SelfDescribing {
    @Override
    <V> SelfDescribingFunction<V, R> compose(Function<? super V, ? extends T> before);

    @Override
    <V> SelfDescribingFunction<T, V> andThen(Function<? super R, ? extends V> after);
}
