package com.dhemery.express;

import org.hamcrest.Matcher;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Composable methods to express assertions and evaluations.
 * @see PolledExpressions
 * @see Named#condition(String, BooleanSupplier)
 * @see NamedBooleanSupplier
 */
public interface Expressions {
    static void assertThat(BooleanSupplier condition) {
        if(!condition.getAsBoolean()) throw new AssertionError(Diagnosis.of(condition));
    }

    /**
     * Assert that subject satisfies the predicate.
     * @throws AssertionError with a diagnostic description if the assertion fails
     * @see Named#predicate(String, Predicate)
     * @see NamedPredicate
     */
    static <T> void assertThat(T subject, Predicate<? super T> predicate) {
        BooleanSupplier condition = new SubjectSatisfiesPredicate<>(subject, predicate);
        if(!condition.getAsBoolean()) throw new AssertionError(Diagnosis.of(condition));
    }

    /**
     * Assert that the characteristic that the function extracts from the subject satisfies the matcher.
     * @throws AssertionError with a diagnostic description if the assertion fails
     * @see Named#function(String, Function)
     * @see NamedFunction
     */
    static <T, R> void assertThat(T subject, Function<? super T, ? extends R> function, Matcher<? super R> matcher) {
        BooleanSupplier condition = new FunctionOfSubjectSatisfiesMatcher<>(subject, function, matcher);
        if(!condition.getAsBoolean()) throw new AssertionError(Diagnosis.of(condition));
    }

    /**
     * Report whether the condition is satisfied.
     */
    static boolean satisfiedThat(BooleanSupplier condition) {
        return condition.getAsBoolean();
    }

    /**
     * Report whether the subject satisfies the matcher.
     */
    static <T> boolean satisfiedThat(T subject, Matcher<? super T> matcher) {
        return matcher.matches(subject);
    }

    /**
     * Report whether the the subject satisfies the predicate.
     */
    static <T> boolean satisfiedThat(T subject, Predicate<? super T> predicate) {
        return predicate.test(subject);
    }

    /**
     * Report whether the characteristic that the function extracts from the subject satisfies the matcher.
     */
    static <T, R> boolean satisfiedThat(T subject, Function<? super T, ? extends R> function, Matcher<? super R> matcher) {
        return matcher.matches(function.apply(subject));
    }
}
