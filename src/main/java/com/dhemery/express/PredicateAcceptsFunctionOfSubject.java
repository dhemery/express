package com.dhemery.express;


import org.hamcrest.Matcher;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A diagnosable boolean supplier
 * that indicates whether
 * a predicate matches
 * the value that a function extracts from a subject.
 * satisfies a predicate.
 * @param <T> the type of the subject
 * @param <R> the type of the value to evaluate
 */
public class PredicateAcceptsFunctionOfSubject<T, R> implements Diagnosable, BooleanSupplier {
    protected final T subject;
    protected final Function<? super T, ? extends R> function;
    private R mostRecentResult;
    private DiagnosingPredicate<? super R> predicate;

    /**
     * Create a diagnosable boolean supplier
     * that indicates whether
     * the predicate accepts
     * the value that the function extracts from the subject.
     * This constructor adapts the predicate
     * to the {@link DiagnosingPredicate} interface
     * if it is not already an instance.
     * @param subject the subject to evaluate
     * @param function the function that extracts the value of interest
     * @param predicate the predicate that evaluates the extracted value
     */
    public PredicateAcceptsFunctionOfSubject(T subject, Function<? super T, ? extends R> function, Predicate<? super R> predicate) {
        this.subject = subject;
        this.function = function;
        this.predicate = diagnosing(predicate);
    }

    /**
     * Create a diagnosable boolean supplier
     * that indicates whether
     * the matcher accepts
     * the value that the function extracts from the subject.
     * This constructor adapts the matcher
     * to the {@link DiagnosingPredicate} interface.
     * @param subject the subject to evaluate
     * @param function the function that extracts the value of interest
     * @param matcher the matcher that evaluates the extracted value
     */
    public PredicateAcceptsFunctionOfSubject(T subject, Function<? super T, ? extends R> function, Matcher<? super R> matcher) {
        this(subject, function, new MatchingPredicate<>(matcher));
    }

    /**
     * {@inheritDoc}
     * @return whether the predicate matches the value extracted from the subject by the function
     */
    @Override
    public boolean getAsBoolean() {
        mostRecentResult = function.apply(subject);
        return predicate.test(mostRecentResult);
    }

    /**
     * {@inheritDoc}
     * @return the subject passed to the constructor
     */
    @Override
    public Optional<String> subject() {
        return Optional.of(String.valueOf(subject));
    }

    /**
     * {@inheritDoc}
     * @return a description of this supplier's function and predicate
     */
    @Override
    public String expectation() {
        return String.format("%s %s", function, predicate);
    }

    /**
     * {@inheritDoc}
     * @return the predicate's reason for rejecting the last extracted value
     */
    @Override
    public Optional<String> failure() {
        return predicate.diagnose(mostRecentResult);
    }

    @SuppressWarnings("unchecked")
    private static <R> DiagnosingPredicate<R> diagnosing(Predicate<R> predicate) {
        if(predicate instanceof DiagnosingPredicate) return DiagnosingPredicate.class.cast(predicate);
        return new NamedDiagnosingPredicate<>(predicate.toString(), predicate);
    }
}
