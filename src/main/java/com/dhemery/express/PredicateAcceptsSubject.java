package com.dhemery.express;

import org.hamcrest.Matcher;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * A diagnosable boolean supplier that indicates whether its predicate accepts
 * its subject.
 *
 * @param <T>
 *         the type of the subject
 */
public class PredicateAcceptsSubject<T> implements Diagnosable, BooleanSupplier {
    private final T subject;
    private final DiagnosingPredicate<? super T> predicate;

    /**
     * Create a diagnosable predicate that indicates whether the predicate
     * accepts the subject. This constructor adapts the predicate to the {@link
     * DiagnosingPredicate} interface if it is not already an instance.
     *
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         the predicate that evaluates the subject
     */
    public PredicateAcceptsSubject(T subject, Predicate<? super T> predicate) {
        this.subject = subject;
        this.predicate = diagnosing(predicate);
    }

    /**
     * Create a diagnosable predicate that indicates whether the matcher accepts
     * the subject. This constructor adapts the matcher to the {@link
     * DiagnosingPredicate} interface.
     *
     * @param subject
     *         the subject to evaluate
     * @param matcher
     *         the matcher that evaluates the subject
     */
    public PredicateAcceptsSubject(T subject, Matcher<? super T> matcher) {
        this(subject, new MatchingPredicate<>(matcher));
    }

    /**
     * @return this predicate's subject
     */
    @Override
    public Optional<String> subject() {
        return Optional.of(String.valueOf(subject));
    }

    /**
     * @return a description of the predicate
     */
    @Override
    public String expectation() {
        return String.valueOf(predicate);
    }

    /**
     * @return whether the predicate accepts the subject
     */
    @Override
    public boolean getAsBoolean() {
        return predicate.test(subject);
    }


    @SuppressWarnings("unchecked")
    private static <R> DiagnosingPredicate<R> diagnosing(Predicate<R> predicate) {
        if (predicate instanceof DiagnosingPredicate)
            return DiagnosingPredicate.class.cast(predicate);
        return new NamedDiagnosingPredicate<>(predicate.toString(), predicate);
    }
}
