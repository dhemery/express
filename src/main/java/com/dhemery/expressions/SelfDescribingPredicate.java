package com.dhemery.expressions;

import org.hamcrest.SelfDescribing;

import java.util.function.Predicate;

/**
 * A {@link Predicate} that can describe itself
 *
 * @param <T>
 *         the type of the input to the predicate
 */
public interface SelfDescribingPredicate<T> extends Predicate<T>, SelfDescribing {
    @Override
    SelfDescribingPredicate<T> and(Predicate<? super T> other);

    @Override
    SelfDescribingPredicate<T> negate();

    @Override
    SelfDescribingPredicate<T> or(Predicate<? super T> other);
}
