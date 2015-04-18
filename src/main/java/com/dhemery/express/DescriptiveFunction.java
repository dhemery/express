package com.dhemery.express;

import java.util.function.Function;

import static java.lang.String.format;


/**
 * A {@link Function} with a fixed description.
 * The {@code toString()} method
 * returns the fixed description.
 * Each composed function
 * created by this function
 * receives a description of the composition.
 * @param <T> the type of the input to the function
 * @param <R> the type of the function result
 */
public class DescriptiveFunction<T, R> extends Descriptive implements Function<T, R> {
    private static final String OF = "(%s of %s)";
    private final Function<? super T, ? extends R> function;

    /**
     * Create a {@link Function}
     * with the given description
     * and underlying function.
     */
    public DescriptiveFunction(String description, Function<? super T, ? extends R> function) {
        super(description);
        this.function = function;
    }

    /**
     * {@inheritDoc}
     * This implementation delegates to the underlying condition.
     */
    @Override
    public R apply(T t) {
        return function.apply(t);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The composed function's description
     * describes this function,
     * the {@code after} function,
     * and their composition.
     * </p>
     */
    @Override
    public <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        return new DescriptiveFunction<>(format(OF, after, this), function.andThen(after));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The composed function's description
     * describes this function,
     * the {@code before} function,
     * and their composition.
     * </p>
     */
    @Override
    public <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        return new DescriptiveFunction<>(format(OF, this, before), function.compose(before));
    }
}
