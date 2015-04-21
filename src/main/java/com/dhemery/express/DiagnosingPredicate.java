package com.dhemery.express;

import org.hamcrest.StringDescription;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * A predicate that can explain why it rejected an input.
 *
 * @param <T>
 *         the type of the input
 */
public interface DiagnosingPredicate<T> extends Predicate<T> {

    /**
     * Describe why this predicate rejected the input.
     *
     * @param input
     *         the rejected input
     *
     * @return a description of why this predicate rejected the input
     */
    default Optional<String> diagnose(T input) {
        return Optional.of("was " + new StringDescription().appendValue(input));
    }
}
