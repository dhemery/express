package com.dhemery.express;

import org.hamcrest.StringDescription;

import java.util.Optional;
import java.util.function.Predicate;

import static java.lang.String.format;

/**
 * A {@link Predicate} that can diagnose a mismatch.
 * Each composed predicate
 * created by this predicate
 * is named to describe the composition.
 * @param <T> the type of the input to the predicate
 */
public class DiagnosingPredicate<T> extends Named implements Predicate<T> {
    private final Predicate<? super T> predicate;

    /**
     * Create a {@link Predicate}
     * with the given name`
     * and underlying predicate.
     */
    public DiagnosingPredicate(String name, Predicate<? super T> predicate) {
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
        return new DiagnosingPredicate<>(format("(%s and %s)", this, other), Predicate.super.and(other));
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
        return new DiagnosingPredicate<>(format("(%s or %s)", this, other), Predicate.super.or(other));
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
        return new DiagnosingPredicate<>(format("(not %s)", this), predicate.negate());
    }

    public Optional<String> diagnose(T result) {
        return Optional.of("was " + new StringDescription().appendValue(result));
    }
}
