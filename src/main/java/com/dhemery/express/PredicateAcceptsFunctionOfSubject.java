package com.dhemery.express;


import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A boolean supplier that evaluates whether its predicate accepts
 * the value that its function derives from its subject.
 *
 * @param <T> the type of the subject
 * @param <R> the type of the result of the function
 * @see Named#condition
 */
public class PredicateAcceptsFunctionOfSubject<T, R> implements BooleanSupplier {
    protected final T subject;
    protected final Function<? super T, ? extends R> function;
    private Predicate<? super R> predicate;

    /**
     * Creates a boolean supplier that evaluates whether the
     * predicate accepts the value that the function derives from the subject.
     *
     * @param subject   the subject to evaluate
     * @param function  the function that derives the value of interest
     * @param predicate the predicate that evaluates the derived value
     */
    public PredicateAcceptsFunctionOfSubject(T subject, Function<? super T, R> function, Predicate<? super R> predicate) {
        this.subject = subject;
        this.function = function;
        this.predicate = predicate;
    }

    /**
     * Evaluates whether the predicate accepts the value that the function derives from the subject.
     *
     * @return true if the predicate accepts the value that the function derives
     * from the subject, otherwise false
     */
    @Override
    public boolean getAsBoolean() {
        return predicate.test(function.apply(subject));
    }
}
