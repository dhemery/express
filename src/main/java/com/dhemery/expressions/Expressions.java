package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.diagnosing.Named;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Methods to compose conditions, evaluate them, and act on the results.
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
    static void assertThat(BooleanSupplier condition) {
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
    static <T> void assertThat(T subject, Predicate<? super T> predicate) {
        if (!predicate.test(subject))
            throw new AssertionError(Diagnosis.of(subject, predicate));
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
    static <T, V> void assertThat(T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        V value = function.apply(subject);
        if (!predicate.test(value))
            throw new AssertionError(Diagnosis.of(subject, function, predicate, value));
    }

    /**
     * Evaluates whether the supplier returns {@code true}.
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
     * Evaluates whether the predicate accepts the subject.
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
     * Evaluates whether the predicate accepts the value that the function derives
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
}
