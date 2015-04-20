package com.dhemery.express;

import org.hamcrest.Matcher;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Composable methods to express assertions and evaluations.
 * @see PolledExpressions
 * @see Named
 * @see NamedBooleanSupplier
 * @see NamedDiagnosingPredicate
 * @see NamedFunction
 */

public interface Expressions {
    /**
     * Assert that
     * the condition is satisfied.
     * @param condition the condition to evaluate
     * @throws AssertionError
     */
    static void assertThat(BooleanSupplier condition) {
        if(!condition.getAsBoolean()) throw new AssertionError(Diagnosis.of(condition));
    }

    /**
     * Assert that
     * the predicate accepts
     * the subject.
     * @param subject the subject to evaluate
     * @param predicate the predicate that evaluates the subject
     * @throws AssertionError
     */
    static <T> void assertThat(T subject, Predicate<? super T> predicate) {
        BooleanSupplier condition = new PredicateAcceptsSubject<>(subject, predicate);
        if(!condition.getAsBoolean()) throw new AssertionError(Diagnosis.of(condition));
    }

    /**
     * Assert that
     * the matcher accepts
     * the subject.
     * @param subject the subject to evaluate
     * @param matcher the matcher that evaluates the subject
     * @throws AssertionError
     */
    static <T> void assertThat(T subject, Matcher<? super T> matcher) {
        BooleanSupplier condition = new PredicateAcceptsSubject<>(subject, matcher);
        if(!condition.getAsBoolean()) throw new AssertionError(Diagnosis.of(condition));
    }

    /**
     * Assert that
     * the matcher accepts
     * the value that the function extracts from the subject.
     * @param subject the subject to evaluate
     * @param function the function that extracts the value of interest
     * @param matcher the matcher that evaluates the extracted value
     * @throws AssertionError
     */
    static <T, R> void assertThat(T subject, Function<? super T, ? extends R> function, Matcher<? super R> matcher) {
        BooleanSupplier condition = new PredicateAcceptsFunctionOfSubject<>(subject, function, matcher);
        if(!condition.getAsBoolean()) throw new AssertionError(Diagnosis.of(condition));
    }

    /**
     * Assert that
     * the predicate accepts
     * the value that the function extracts from the subject.
     * @param subject the subject to evaluate
     * @param function the function that extracts the value of interest
     * @param predicate the predicate that evaluates the extracted value
     * @throws AssertionError
     */
    static <T, R> void assertThat(T subject, Function<? super T, ? extends R> function, Predicate<? super R> predicate) {
        BooleanSupplier condition = new PredicateAcceptsFunctionOfSubject<>(subject, function, predicate);
        if(!condition.getAsBoolean()) throw new AssertionError(Diagnosis.of(condition));
    }

    /**
     * Indicate whether
     * the condition is satisfied.
     * @param condition the condition to evaluate
     */
    static boolean satisfiedThat(BooleanSupplier condition) {
        return condition.getAsBoolean();
    }

    /**
     * Indicate whether
     * the matcher
     * accepts the subject.
     * @param subject the subject to evaluate
     * @param matcher the matcher that evaluates the subject
     */
    static <T> boolean satisfiedThat(T subject, Matcher<? super T> matcher) {
        return matcher.matches(subject);
    }

    /**
     * Indicate whether
     * the predicate accepts
     * the subject.
     * @param subject the subject to evaluate
     * @param predicate the predicate that evaluates the subject
     */
    static <T> boolean satisfiedThat(T subject, Predicate<? super T> predicate) {
        return predicate.test(subject);
    }

    /**
     * Indicate whether
     * the matcher accepts
     * the value that the function extracts from the subject.
     * @param subject the subject to evaluate
     * @param function the function that extracts the value of interest
     * @param matcher the matcher that evaluates the extracted value
     */
    static <T, R> boolean satisfiedThat(T subject, Function<? super T, ? extends R> function, Matcher<? super R> matcher) {
        return matcher.matches(function.apply(subject));
    }

    /**
     * Indicate whether
     * the predicate accepts
     * the value that the function extracts from the subject.
     * @param subject the subject to evaluate
     * @param function the function that extracts the value of interest
     * @param predicate the predicate that evaluates the extracted value
     */
    static <T, R> boolean satisfiedThat(T subject, Function<? super T, ? extends R> function, Predicate<? super R> predicate) {
        return predicate.test(function.apply(subject));
    }
}
