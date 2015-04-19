package com.dhemery.express;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.StringDescription;

import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.dhemery.express.Descriptive.describedAs;
import static java.util.stream.Collectors.joining;

/**
 * Composable methods to express assertions and evaluations.
 * These methods evaluate their expressions immediately, without polling.
 */
public interface Expressions {
    static void assertThat(Condition condition) {
        if(condition.isSatisfied()) return;
        StringJoiner message = new StringJoiner(" ")
                .add("Expected:")
                .add(String.valueOf(condition));
        condition.diagnosis().ifPresent(d -> message.add("\n     but:").add(d));
        throw new AssertionError(message);
    }

    /**
     * Assert that subject satisfies the predicate.
     * @throws AssertionError with a diagnostic description if the assertion fails
     */
    static <T> void assertThat(T subject, Predicate<? super T> predicate) {
        if(predicate.test(subject)) return;
        String message = describedAs("Expected:", subject, predicate);
        throw new AssertionError(message);
    }

    /**
     * Assert that the characteristic that the function extracts from the subject satisfies the matcher.
     * @throws AssertionError with a diagnostic description if the assertion fails
     */
    static <T, R> void assertThat(T subject, Function<? super T, ? extends R> function, Matcher<? super R> matcher) {
        R characteristic = function.apply(subject);
        if(matcher.matches(characteristic)) return;
        String failedExpectation = describedAs("Expected:", subject, function, matcher);
        Description diagnosis = new StringDescription();
        diagnosis.appendText("     but: ");
        matcher.describeMismatch(characteristic, diagnosis);
        StringJoiner message = new StringJoiner("\n");
        message.add(failedExpectation).add(diagnosis.toString());
        throw new AssertionError(message);
    }

    /**
     * Report whether the condition is satisfied.
     */
    static boolean satisfiedThat(Condition condition) {
        return condition.isSatisfied();
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
