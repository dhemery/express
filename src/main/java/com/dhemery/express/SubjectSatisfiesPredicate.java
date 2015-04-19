package com.dhemery.express;

import java.util.Optional;
import java.util.function.Predicate;

import static java.lang.String.format;

public class SubjectSatisfiesPredicate<T> extends Named implements Condition {
    private final T subject;
    private final Predicate<? super T> predicate;

    public SubjectSatisfiesPredicate(T subject, Predicate<? super T> predicate) {
        super(format("%s %s", subject, predicate));
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
