package com.dhemery.express;

import java.util.function.Predicate;

import static java.lang.String.format;

/**
 * A named predicate that can diagnose a rejection. Each composed predicate
 * created by this predicate is named to describe the composition.
 *
 * @param <T>
 *         the type of the input to the predicate
 */
public class NamedDiagnosingPredicate<T> extends Named implements DiagnosingPredicate<T> {
    private final Predicate<? super T> predicate;

    /**
     * Create a named diagnosing predicate.
     *
     * @param name
     *         the name of the predicate
     * @param predicate
     *         the underlying predicate
     */
    public NamedDiagnosingPredicate(String name, Predicate<? super T> predicate) {
        super(name);
        this.predicate = predicate;
    }

    /**
     * {@inheritDoc}
     *
     * @return the value returned by the underlying predicate
     */
    @Override
    public boolean test(T t) {
        return predicate.test(t);
    }

    /**
     * {@inheritDoc} <p> The composed predicate is named to describe this
     * predicate, the other predicate, and their composition. </p>
     */
    @Override
    public Predicate<T> and(Predicate<? super T> other) {
        return new NamedDiagnosingPredicate<>(format("(%s and %s)", this, other), DiagnosingPredicate.super.and(other));
    }

    /**
     * {@inheritDoc} <p> The composed predicate is named to describe this
     * predicate, the other predicate, and their composition. </p>
     */
    @Override
    public Predicate<T> or(Predicate<? super T> other) {
        return new NamedDiagnosingPredicate<>(format("(%s or %s)", this, other), DiagnosingPredicate.super.or(other));
    }

    /**
     * {@inheritDoc} <p> The composed predicate is named to describe this
     * predicate, with the word "not" prepended. </p>
     */
    @Override
    public Predicate<T> negate() {
        return new NamedDiagnosingPredicate<>(format("(not %s)", this), predicate.negate());
    }
}
