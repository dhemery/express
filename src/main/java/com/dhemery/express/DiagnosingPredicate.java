package com.dhemery.express;

import org.hamcrest.StringDescription;

import java.util.Optional;
import java.util.function.Predicate;

public interface DiagnosingPredicate<T> extends Predicate<T> {

    /**
     * Describe why this predicate rejected the result.
     * @param result the rejected result
     * @return a description of why this predicate rejected the result
     */
    default Optional<String> diagnose(T result) {
        return Optional.of("was " + new StringDescription().appendValue(result));
    }
}
