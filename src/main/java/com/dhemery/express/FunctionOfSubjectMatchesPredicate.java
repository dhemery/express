package com.dhemery.express;


import org.hamcrest.Matcher;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

public class FunctionOfSubjectMatchesPredicate<T, R> implements Diagnosable, BooleanSupplier {
    protected final T subject;
    protected final Function<? super T, ? extends R> function;
    private R mostRecentResult;
    private DiagnosingPredicate<? super R> predicate;

    public FunctionOfSubjectMatchesPredicate(T subject, Function<? super T, ? extends R> function, Matcher<? super R> matcher) {
        this(subject, function, new MatchingPredicate<>(matcher));
    }

    public FunctionOfSubjectMatchesPredicate(T subject, Function<? super T, ? extends R> function, Predicate<? super R> predicate) {
        this(subject, function, new DiagnosingPredicate<>(predicate.toString(), predicate));
    }

    public FunctionOfSubjectMatchesPredicate(T subject, Function<? super T, ? extends R> function, DiagnosingPredicate<? super R> predicate) {
        this.subject = subject;
        this.function = function;
        this.predicate = predicate;
    }

    @Override
    public boolean getAsBoolean() {
        mostRecentResult = function.apply(subject);
        return predicate.test(mostRecentResult);
    }

    @Override
    public Optional<String> subject() {
        return Optional.of(String.valueOf(subject));
    }

    @Override
    public String expectation() {
        return String.format("%s %s", function, predicate);
    }

    @Override
    public Optional<String> failure() {
        return predicate.diagnose(mostRecentResult);
    }
}
