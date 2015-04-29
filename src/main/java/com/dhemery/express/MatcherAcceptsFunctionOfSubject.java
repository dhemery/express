package com.dhemery.express;

import org.hamcrest.Matcher;

import java.util.function.BooleanSupplier;
import java.util.function.Function;


public class MatcherAcceptsFunctionOfSubject<T, R>  implements BooleanSupplier {
    private final T subject;
    private final Function<? super T, R> function;
    private final Matcher<? super R> matcher;

    public MatcherAcceptsFunctionOfSubject(T subject, Function<? super T, R> function, Matcher<? super R> matcher) {
        this.subject = subject;
        this.function = function;
        this.matcher = matcher;
    }

    @Override
    public boolean getAsBoolean() {
        return matcher.matches(function.apply(subject));
    }
}
