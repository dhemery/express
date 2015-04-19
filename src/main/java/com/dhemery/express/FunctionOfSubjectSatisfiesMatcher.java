package com.dhemery.express;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class FunctionOfSubjectSatisfiesMatcher<T, R> extends Named implements Condition {
    private final T subject;
    private final Function<? super T, R> function;
    private final Matcher<? super R> matcher;
    private R mostRecentResult;

    public FunctionOfSubjectSatisfiesMatcher(T subject, Function<? super T, R> function, Matcher<? super R> matcher) {
        super(format("%s %s %s", subject, function, matcher));
        this.subject = subject;
        this.function = function;
        this.matcher = matcher;
    }

    @Override
    public boolean getAsBoolean() {
        mostRecentResult = function.apply(subject);
        return matcher.matches(mostRecentResult);
    }

    @Override
    public Optional<String> subject() {
        return Optional.of(String.valueOf(subject));
    }

    @Override
    public String expectation() {
        return format("%s %s", function, matcher);
    }

    @Override
    public Optional<String> failure() {
        Description mismatchDescription = new StringDescription();
        matcher.describeMismatch(mostRecentResult, mismatchDescription);
        return Optional.of(mismatchDescription.toString());
    }
}
