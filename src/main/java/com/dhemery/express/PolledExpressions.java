package com.dhemery.express;

import org.hamcrest.Matcher;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Expressive methods to poll composed conditions.
 *
 * @see Expressions
 * @see Poller
 * @see Named
 * @see NamedBooleanSupplier
 * @see NamedDiagnosingPredicate
 * @see NamedFunction
 */
public interface PolledExpressions extends Poller {
    /**
     * Assert that the condition is satisfied within the schedule's duration.
     *
     * @param schedule
     *         the schedule that governs the polling
     * @param condition
     *         the condition to satisfy
     */
    default void assertThat(PollingSchedule schedule, BooleanSupplier condition) {
        if (!poll(schedule, condition))
            throw new AssertionError(Diagnosis.of(schedule, condition));
    }

    /**
     * Assert that the predicate accepts the subject within the schedule's
     * duration.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         the predicate that evaluates the subject
     * @param schedule
     *         the schedule that governs the polling
     */
    default <T> void assertThat(T subject, PollingSchedule schedule, Predicate<? super T> predicate) {
        BooleanSupplier condition = new PredicateAcceptsSubject<>(subject, predicate);
        if (!poll(schedule, condition))
            throw new AssertionError(Diagnosis.of(schedule, condition));
    }

    /**
     * Assert that the matcher accepts the value that the function extracts from
     * the subject within the schedule's duration.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that extracts the value of interest
     * @param matcher
     *         the matcher that evaluates the extracted value
     * @param schedule
     *         the schedule that governs the polling
     */
    default <T, V> void assertThat(T subject, Function<? super T, V> function, PollingSchedule schedule, Matcher<? super V> matcher) {
        BooleanSupplier condition = new PredicateAcceptsFunctionOfSubject<>(subject, function, matcher);
        if (!poll(schedule, condition))
            throw new AssertionError(Diagnosis.of(schedule, condition));
    }

    /**
     * Assert that the predicate accepts the value that the function extracts
     * from the subject within the schedule's duration.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that extracts the value of interest
     * @param predicate
     *         the predicate that evaluates the extracted value
     * @param schedule
     *         the schedule that governs the polling
     */
    default <T, V> void assertThat(T subject, Function<? super T, V> function, PollingSchedule schedule, Predicate<? super V> predicate) {
        BooleanSupplier condition = new PredicateAcceptsFunctionOfSubject<>(subject, function, predicate);
        if (!poll(schedule, condition))
            throw new AssertionError(Diagnosis.of(schedule, condition));
    }

    /**
     * Indicate whether the condition is satisfied within the schedule's
     * duration.
     *
     * @param schedule
     *         the schedule that governs the polling
     * @param condition
     *         the condition to satisfy
     *
     * @return {@code true} if the condition is satisfied with the schedule's
     * duration, and {@code false} otherwise.
     */
    default boolean satisfiedThat(PollingSchedule schedule, BooleanSupplier condition) {
        return poll(schedule, condition);
    }

    /**
     * Indicate whether the predicate accepts the subject within the schedule's
     * duration.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         the predicate that evaluates the subject
     * @param schedule
     *         the schedule that governs the polling
     *
     * @return {@code true} if the condition is satisfied within the schedule's
     * duration, and {@code false} otherwise.
     */
    default <T> boolean satisfiedThat(T subject, PollingSchedule schedule, Predicate<? super T> predicate) {
        return poll(schedule, new PredicateAcceptsSubject<>(subject, predicate));
    }

    /**
     * Indicate whether the matcher accepts the value that the function extracts
     * from the subject within the schedule's duration.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that extracts the value of interest
     * @param matcher
     *         the matcher that evaluates the extracted value
     * @param schedule
     *         the schedule that governs the polling
     *
     * @return {@code true} if the condition is satisfied within the schedule's
     * duration, and {@code false} otherwise.
     */
    default <T, V> boolean satisfiedThat(T subject, Function<? super T, V> function, PollingSchedule schedule, Matcher<? super V> matcher) {
        return poll(schedule, new PredicateAcceptsFunctionOfSubject<>(subject, function, matcher));
    }

    /**
     * Indicate whether the predicate accepts the value that the function
     * extracts from the subject within the schedule's duration.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that extracts the value of interest
     * @param predicate
     *         the predicate that evaluates the extracted value
     * @param schedule
     *         the schedule that governs the polling
     *
     * @return {@code true} if the condition is satisfied within the schedule's
     * duration, and {@code false} otherwise.
     */
    default <T, V> boolean satisfiedThat(T subject, Function<? super T, V> function, PollingSchedule schedule, Predicate<? super V> predicate) {
        return poll(schedule, new PredicateAcceptsFunctionOfSubject<>(subject, function, predicate));
    }

    /**
     * Wait until the condition is satisfied.
     *
     * @param condition
     *         the condition to satisfy
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default void waitUntil(BooleanSupplier condition) {
        if (!poll(condition)) throw new PollTimeoutException(condition);
    }

    /**
     * Wait until the condition is satisfied.
     *
     * @param schedule
     *         the schedule that governs the polling
     * @param condition
     *         the condition to satisfy
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default void waitUntil(PollingSchedule schedule, BooleanSupplier condition) {
        if (!poll(schedule, condition))
            throw new PollTimeoutException(schedule, condition);
    }

    /**
     * Wait until the predicate accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         the predicate that evaluates the subject
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default <T> void waitUntil(T subject, Predicate<? super T> predicate) {
        BooleanSupplier condition = new PredicateAcceptsSubject<>(subject, predicate);
        if (!poll(condition)) throw new PollTimeoutException(condition);
    }

    /**
     * Wait until the predicate accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         the predicate that evaluates the subject
     * @param schedule
     *         the schedule that governs the polling
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default <T> void waitUntil(T subject, PollingSchedule schedule, Predicate<? super T> predicate) {
        BooleanSupplier condition = new PredicateAcceptsSubject<>(subject, predicate);
        if (!poll(schedule, condition))
            throw new PollTimeoutException(schedule, condition);
    }

    /**
     * Wait until the matcher accepts the value that the function extracts from
     * the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that extracts the value of interest
     * @param matcher
     *         the matcher that evaluates the extracted value
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default <T, R> void waitUntil(T subject, Function<? super T, R> function, Matcher<? super R> matcher) {
        BooleanSupplier condition = new PredicateAcceptsFunctionOfSubject<>(subject, function, matcher);
        if (!poll(condition)) throw new PollTimeoutException(condition);
    }

    /**
     * Wait until the predicate accepts the value that the function extracts
     * from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that extracts the value of interest
     * @param predicate
     *         the predicate that evaluates the extracted value
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default <T, R> void waitUntil(T subject, Function<? super T, R> function, Predicate<? super R> predicate) {
        BooleanSupplier condition = new PredicateAcceptsFunctionOfSubject<>(subject, function, predicate);
        if (!poll(condition)) throw new PollTimeoutException(condition);
    }

    /**
     * Wait until the matcher accepts the value that the function extracts from
     * the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that extracts the value of interest
     * @param matcher
     *         the matcher that evaluates the extracted value
     * @param schedule
     *         the schedule that governs the polling
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default <T, R> void waitUntil(T subject, Function<? super T, R> function, PollingSchedule schedule, Matcher<? super R> matcher) {
        BooleanSupplier condition = new PredicateAcceptsFunctionOfSubject<>(subject, function, matcher);
        if (!poll(schedule, condition))
            throw new PollTimeoutException(schedule, condition);
    }

    /**
     * Wait until the predicate accepts the value that the function extracts
     * from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that extracts the value of interest
     * @param predicate
     *         the predicate that evaluates the extracted value
     * @param schedule
     *         the schedule that governs the polling
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default <T, R> void waitUntil(T subject, Function<? super T, R> function, PollingSchedule schedule, Predicate<? super R> predicate) {
        BooleanSupplier condition = new PredicateAcceptsFunctionOfSubject<>(subject, function, predicate);
        if (!poll(schedule, condition))
            throw new PollTimeoutException(schedule, condition);
    }

    /**
     * Return the subject when the predicate accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         the predicate that evaluates the subject
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default <T> T when(T subject, Predicate<? super T> predicate) {
        BooleanSupplier condition = new PredicateAcceptsSubject<>(subject, predicate);
        if (!poll(condition)) throw new PollTimeoutException(condition);
        return subject;
    }

    /**
     * Return the subject when the predicate accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         the predicate that evaluates the subject
     * @param schedule
     *         the schedule that governs the polling
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default <T> T when(T subject, PollingSchedule schedule, Predicate<? super T> predicate) {
        BooleanSupplier condition = new PredicateAcceptsSubject<>(subject, predicate);
        if (!poll(schedule, condition))
            throw new PollTimeoutException(schedule, condition);
        return subject;
    }

    /**
     * Return the subject when the matcher accepts the value that the function
     * extracts from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that extracts the value of interest
     * @param matcher
     *         the matcher that evaluates the extracted value
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default <T, R> T when(T subject, Function<? super T, R> function, Matcher<? super R> matcher) {
        BooleanSupplier condition = new PredicateAcceptsFunctionOfSubject<>(subject, function, matcher);
        if (!poll(condition)) throw new PollTimeoutException(condition);
        return subject;
    }

    /**
     * Return the subject when the predicate accepts the value that the function
     * extracts from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that extracts the value of interest
     * @param predicate
     *         the predicate that evaluates the extracted value
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default <T, R> T when(T subject, Function<? super T, R> function, Predicate<? super R> predicate) {
        BooleanSupplier condition = new PredicateAcceptsFunctionOfSubject<>(subject, function, predicate);
        if (!poll(condition)) throw new PollTimeoutException(condition);
        return subject;
    }

    /**
     * Return the subject when the matcher accepts the value that the function
     * extracts from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that extracts the value of interest
     * @param matcher
     *         the matcher that evaluates the extracted value
     * @param schedule
     *         the schedule that governs the polling
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default <T, R> T when(T subject, Function<? super T, R> function, PollingSchedule schedule, Matcher<? super R> matcher) {
        BooleanSupplier condition = new PredicateAcceptsFunctionOfSubject<>(subject, function, matcher);
        if (!poll(schedule, condition))
            throw new PollTimeoutException(schedule, condition);
        return subject;
    }

    /**
     * Return the subject when the predicate accepts the value that the function
     * extracts from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that extracts the value of interest
     * @param predicate
     *         the predicate that evaluates the extracted value
     * @param schedule
     *         the schedule that governs the polling
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default <T, R> T when(T subject, Function<? super T, R> function, PollingSchedule schedule, Predicate<? super R> predicate) {
        BooleanSupplier condition = new PredicateAcceptsFunctionOfSubject<>(subject, function, predicate);
        if (!poll(schedule, condition))
            throw new PollTimeoutException(schedule, condition);
        return subject;
    }
}
