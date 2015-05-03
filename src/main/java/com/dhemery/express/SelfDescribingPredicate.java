package com.dhemery.express;

import org.hamcrest.SelfDescribing;

import java.util.function.Predicate;

public interface SelfDescribingPredicate<T> extends Predicate<T>, SelfDescribing {
    @Override
    SelfDescribingPredicate<T> and(Predicate<? super T> other);

    @Override
    SelfDescribingPredicate<T> negate();

    @Override
    SelfDescribingPredicate<T> or(Predicate<? super T> other);
}
