package com.dhemery.express;

import java.util.function.Predicate;

import static java.lang.String.format;

/**
 * A {@link Predicate} with a fixed name.
 * The {@code toString()} method
 * returns the fixed name.
 * Each composed predicate
 * created by this predicate
 * is named to describe the composition.
 * @param <T> the type of the input to the predicate
 */
public class NamedPredicate<T> extends Named implements Predicate<T> {
    private final Predicate<? super T> predicate;

    /**
     * Create a {@link Predicate}
     * with the given name`
     * and underlying predicate.
     */
    public NamedPredicate(String name, Predicate<? super T> predicate) {
        super(name);
        this.predicate = predicate;
    }

    /**
     * @return the value returned by the underlying predicate
     */
    @Override
    public boolean test(T t) {
        return predicate.test(t);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The composed predicate is named to describe
     * this predicate,
     * the {@code other} predicate,
     * and their composition.
     * </p>
     */
    @Override
    public Predicate<T> and(Predicate<? super T> other) {
        return new NamedPredicate<>(format("(%s and %s)", this, other), Predicate.super.and(other));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The composed predicate is named to describe
     * this predicate,
     * the {@code other} predicate,
     * and their composition.
     * </p>
     */
    @Override
    public Predicate<T> or(Predicate<? super T> other) {
        return new NamedPredicate<>(format("(%s or %s)", this, other), Predicate.super.or(other));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The composed predicate is named to describe
     * this predicate,
     * with the word "not" prepended.
     * </p>
     */
    @Override
    public Predicate<T> negate() {
        return new NamedPredicate<>(format("(not %s)", this), predicate.negate());
    }
}
