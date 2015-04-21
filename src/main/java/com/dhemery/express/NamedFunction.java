package com.dhemery.express;

import java.util.function.Function;

import static java.lang.String.format;


/**
 * A function with a fixed name. The {@code toString()} method returns the fixed
 * name. Each composed function created by this function is named to describe
 * the composition.
 *
 * @param <T>
 *         the type of the input to the function
 * @param <R>
 *         the type of the function result
 */
public class NamedFunction<T, R> extends Named implements Function<T, R> {
    private final Function<? super T, ? extends R> function;

    /**
     * Create a named function.
     *
     * @param name
     *         the name of this function
     * @param function
     *         the underlying function.
     */
    public NamedFunction(String name, Function<? super T, ? extends R> function) {
        super(name);
        this.function = function;
    }

    /**
     * {@inheritDoc}
     *
     * @return the value returned by the underlying function
     */
    @Override
    public R apply(T t) {
        return function.apply(t);
    }

    /**
     * {@inheritDoc} <p> The composed function is named to describe this
     * function, the {@code after} function, and their composition. </p>
     */
    @Override
    public <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        return new NamedFunction<>(format("(%s of %s)", after, this), function.andThen(after));
    }

    /**
     * {@inheritDoc} <p> The composed function is named to describe this
     * function, the {@code before} function, and their composition. </p>
     */
    @Override
    public <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        return new NamedFunction<>(format("(%s of %s)", this, before), function.compose(before));
    }
}
