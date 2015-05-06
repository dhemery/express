package com.dhemery.expressions.diagnosing;

import com.dhemery.expressions.SelfDescribingPredicate;

import java.util.function.Predicate;

import static java.lang.String.format;

/**
 * A {@link Predicate} that describes itself by name. Each composed predicate
 * created by this predicate is named to describe the composition.
 *
 * @param <T>
 *         the type of the input to the predicate
 */
public class NamedPredicate<T> extends DescribedByName implements SelfDescribingPredicate<T> {
    private final Predicate<T> predicate;

    /**
     * Create a named diagnosing predicate.
     *
     * @param name
     *         the name of the predicate
     * @param predicate
     *         the underlying predicate
     */
    public NamedPredicate(String name, Predicate<T> predicate) {
        super(name);
        this.predicate = predicate;
    }

    /**
     * @return whether the underlying predicate accepts the input argument
     */
    @Override
    public boolean test(T t) {
        return predicate.test(t);
    }

    /**
     * {@inheritDoc} The composed predicate is named to describe this predicate,
     * the other predicate, and their composition.
     */
    @Override
    public SelfDescribingPredicate<T> and(Predicate<? super T> other) {
        return new NamedPredicate<>(format("(%s and %s)", this, BestDescription.of(other)), predicate.and(other));
    }

    /**
     * {@inheritDoc} The composed predicate is named to describe this predicate,
     * the other predicate, and their composition.
     */
    @Override
    public SelfDescribingPredicate<T> or(Predicate<? super T> other) {
        return new NamedPredicate<>(format("(%s or %s)", this, BestDescription.of(other)), predicate.or(other));
    }

    /**
     * {@inheritDoc} The composed predicate is named to describe this predicate,
     * with the word "not" prepended.
     */
    @Override
    public SelfDescribingPredicate<T> negate() {
        return new NamedPredicate<>(format("(not %s)", this), predicate.negate());
    }
}
