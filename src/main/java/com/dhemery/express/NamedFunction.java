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
 *         the type of the result of the function
 */
public class NamedFunction<T, R> extends Named implements SelfDescribingFunction<T, R> {
    private final Function<T, R> function;

    /**
     * Create a named function.
     *
     * @param name
     *         the name of this function
     * @param function
     *         the underlying function.
     */
    public NamedFunction(String name, Function<T, R> function) {
        super(name);
        this.function = function;
    }

    /**
     * @return the value returned by applying the underlying function to the
     * function argument
     */
    @Override
    public R apply(T t) {
        return function.apply(t);
    }

    /**
     * {@inheritDoc} The composed function is named to describe this function,
     * the {@code after} function, and their composition.
     */
    @Override
    public <V> SelfDescribingFunction<T, V> andThen(Function<? super R, ? extends V> after) {
        return new NamedFunction<>(format("(%s of %s)", BestDescription.of(after), this), function.andThen(after));
    }

    /**
     * {@inheritDoc} The composed function is named to describe this function,
     * the {@code before} function, and their composition.
     */
    @Override
    public <V> SelfDescribingFunction<V, R> compose(Function<? super V, ? extends T> before) {
        return new NamedFunction<>(format("(%s of %s)", this, BestDescription.of(before)), function.compose(before));
    }
}
