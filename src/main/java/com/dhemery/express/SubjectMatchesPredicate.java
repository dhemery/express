package com.dhemery.express;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class SubjectMatchesPredicate<T> implements Diagnosable, BooleanSupplier {
    private final T subject;
    private final Predicate<? super T> predicate;

    public SubjectMatchesPredicate(T subject, Predicate<? super T> predicate) {
        this.subject = subject;
        this.predicate = predicate;
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
}
