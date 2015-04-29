package com.dhemery.express;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * A boolean supplier that indicates whether its predicate accepts its subject.
 *
 * @param <T>
 *         the type of the subject
 */
public class PredicateAcceptsSubject<T> implements BooleanSupplier {
    private final T subject;
    private final Predicate<? super T> predicate;

    /**
     * Creates a boolean supplier that indicates whether the predicate accepts the subject.
     *
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         the predicate that evaluates the subject
     */
    public PredicateAcceptsSubject(T subject, Predicate<? super T> predicate) {
        this.subject = subject;
        this.predicate = predicate;
    }

    /**
     * Evaluates whether the predicate accepts the subject.
     *
     * @return true if the predicate accepts the subject, otherwise false
     */
    @Override
    public boolean getAsBoolean() {
        return predicate.test(subject);
    }
}
