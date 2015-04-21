package com.dhemery.express;

import org.hamcrest.Matcher;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * A diagnosable boolean supplier that indicates whether a predicate matches a
 * subject.
 *
 * @param <T>
 *         the type of the subject
 */
public class PredicateAcceptsSubject<T> implements Diagnosable, BooleanSupplier {
    private final T subject;
    private final DiagnosingPredicate<? super T> predicate;

    public PredicateAcceptsSubject(T subject, Predicate<? super T> predicate) {
        this.subject = subject;
        this.predicate = diagnosing(predicate);
    }

    public PredicateAcceptsSubject(T subject, Matcher<? super T> matcher) {
        this(subject, new MatchingPredicate<>(matcher));
    }

    @Override
    public Optional<String> subject() {
        return Optional.of(String.valueOf(subject));
    }

    @Override
    public String expectation() {
        return String.valueOf(predicate);
    }

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
