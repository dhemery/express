package com.dhemery.express;

import org.hamcrest.Matcher;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Expressive methods to evaluate composed conditions.
 *
 * @see PolledExpressions
 * @see Named
 */

public interface Expressions {
    /**
     * Asserts that the supplier returns {@code true}.
     *
     * @param condition
     *         the supplier to evaluate
     *
     * @throws AssertionError
     *         if the supplier returns {@code false}
     */
    static void assertThat(SelfDescribingBooleanSupplier condition) {
        if (!condition.getAsBoolean())
            throw new AssertionError(Diagnosis.of(condition));
    }

    /**
     * Asserts that the predicate accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         the predicate that evaluates the subject
     */
    static <T> void assertThat(T subject, SelfDescribingPredicate<? super T> predicate) {
        if (!predicate.test(subject))
            throw new AssertionError(Diagnosis.of(subject, predicate));
    }

    /**
     * Asserts that the matcher accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param matcher
     *         the matcher that evaluates the subject
     */
    static <T> void assertThat(T subject, Matcher<? super T> matcher) {
        if (!matcher.matches(subject))
            throw new AssertionError(Diagnosis.of(subject, matcher));
    }

    /**
     * Asserts that the predicate accepts the value that the function derives
     * from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <V>
     *         the type of the derived value
     * @param subject
     *         the subject to evaluate
     * @param function
     *         derives the value of interest from the subject
     * @param predicate
     *         evaluates the derived value
     */
    static <T, V> void assertThat(T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate) {
        V value = function.apply(subject);
        if (!predicate.test(value))
            throw new AssertionError(Diagnosis.of(subject, function, predicate, value));
    }

    /**
     * Asserts that the matcher accepts the value that the function derives from
     * the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <V>
     *         the type of the derived value
     * @param subject
     *         the subject to evaluate
     * @param function
     *         derives the value of interest from the subject
     * @param matcher
     *         evaluates the derived value
     */
    static <T, V> void assertThat(T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher) {
        V value = function.apply(subject);
        if (!matcher.matches(value))
            throw new AssertionError(Diagnosis.of(subject, function, matcher, value));
    }

    /**
     * Returns whether the supplier returns {@code true}.
     *
     * @param supplier
     *         the supplier to evaluate
     *
     * @return the value returned by the supplier
     */
    static boolean satisfiedThat(BooleanSupplier supplier) {
        return supplier.getAsBoolean();
    }

    /**
     * Returns whether the predicate accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         the predicate that evaluates the subject
     *
     * @return {@code true} if the predicate accepts the subject, otherwise
     * {@code false}
     */
    static <T> boolean satisfiedThat(T subject, Predicate<? super T> predicate) {
        return predicate.test(subject);
    }

    /**
     * Returns whether the matcher accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param matcher
     *         the matcher that evaluates the subject
     *
     * @return {@code true} if the matcher accepts the subject, otherwise {@code
     * false}
     */
    static <T> boolean satisfiedThat(T subject, Matcher<? super T> matcher) {
        return matcher.matches(subject);
    }

    /**
     * Returns whether the predicate accepts the value that the function derives
     * from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <V>
     *         the type of the derived value
     * @param subject
     *         the subject to evaluate
     * @param function
     *         derives the value of interest from the subject
     * @param predicate
     *         evaluates the derived value
     *
     * @return {@code true} if the predicate accepts the value that the function
     * derives from subject, otherwise {@code false}
     */
    static <T, V> boolean satisfiedThat(T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        return predicate.test(function.apply(subject));
    }

    /**
     * Returns whether the matcher accepts the value that the function derives
     * from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <V>
     *         the type of the derived value
     * @param subject
     *         the subject to evaluate
     * @param function
     *         derives the value of interest from the subject
     * @param matcher
     *         evaluates the derived value
     *
     * @return {@code true} if the matcher accepts the value that the function
     * derives from the subject, otherwise {@code false}
     */
    static <T, V> boolean satisfiedThat(T subject, Function<? super T, V> function, Matcher<? super V> matcher) {
        return matcher.matches(function.apply(subject));
    }
}
