package com.dhemery.expressions;

import org.hamcrest.SelfDescribing;

import java.util.function.Function;

public interface SelfDescribingFunction<T, R> extends Function<T, R>, SelfDescribing {
    @Override
    <V> SelfDescribingFunction<V, R> compose(Function<? super V, ? extends T> before);

    @Override
    <V> SelfDescribingFunction<T, V> andThen(Function<? super R, ? extends V> after);
}
