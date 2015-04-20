package com.dhemery.express;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.Optional;

/**
 * A {@link DiagnosingPredicate} that delegates to a {@link Matcher}.
 * @param <T> the type of input to the predicate
 */
public class MatchingPredicate<T> extends DiagnosingPredicate<T> {
    private final Matcher<? super T> matcher;

    public MatchingPredicate(Matcher<? super T> matcher) {
        super(StringDescription.toString(matcher), matcher::matches);
        this.matcher = matcher;
    }

    @Override
    public Optional<String> diagnose(T result) {
        Description mismatch = new StringDescription();
        matcher.describeMismatch(result, mismatch);
        return Optional.of(mismatch.toString());
    }
}
