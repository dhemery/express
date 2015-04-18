package com.dhemery.express;

import java.util.function.Predicate;

import static java.lang.String.format;

/**
 * A {@link Predicate} with a fixed description.
 * The {@code toString()} method
 * returns the fixed description.
 * Each composed predicate
 * created by this predicate
 * receives a description of the composition.
 * @param <T> the type of the input to the predicate
 */
public class DescriptivePredicate<T> extends Descriptive implements Predicate<T> {
    public static final String NEGATE = "(not %s)";
    public static final String AND = "(%s and %s)";
    public static final String OR = "(%s or %s)";
    private final Predicate<? super T> predicate;

    /**
     * Create a {@link Predicate}
     * with the given description
     * and underlying predicate.
     */
    public DescriptivePredicate(String description, Predicate<? super T> predicate) {
        super(description);
        this.predicate = predicate;
    }

    /**
     * {@inheritDoc}
     * This implementation delegates to the underlying predicate.
     */
    @Override
    public boolean test(T t) {
        return predicate.test(t);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The composed predicate's description
     * describes this predicate,
     * the {@code other} predicate,
     * and their composition.
     * </p>
     */
    @Override
    public Predicate<T> and(Predicate<? super T> other) {
        return new DescriptivePredicate<>(format(AND, this, other), Predicate.super.and(other));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The composed predicate's description
     * describes this predicate,
     * the {@code other} predicate,
     * and their composition.
     * </p>
     */
    @Override
    public Predicate<T> or(Predicate<? super T> other) {
        return new DescriptivePredicate<>(format(OR, this, other), Predicate.super.or(other));
    }

    /**
     * {@inheritDoc}
     * <p>
     * The composed predicate's description
     * is this predicate's description
     * with the word "not" prepended.
     * </p>
     */
    @Override
    public Predicate<T> negate() {
        return new DescriptivePredicate<>(format(NEGATE, this), predicate.negate());
    }
}
