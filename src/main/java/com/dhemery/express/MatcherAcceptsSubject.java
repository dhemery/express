package com.dhemery.express;

import org.hamcrest.Matcher;

import java.util.function.BooleanSupplier;

public class MatcherAcceptsSubject<T> implements BooleanSupplier {
    private final T subject;
    private final Matcher<? super T> matcher;

    public MatcherAcceptsSubject(T subject, Matcher<? super T> matcher) {
        this.subject = subject;
        this.matcher = matcher;
    }

    @Override
    public boolean getAsBoolean() {
        return matcher.matches(subject);
    }
}
