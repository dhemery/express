package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.polling.PollEvaluationResult;
import com.dhemery.expressions.polling.PollTimeoutException;
import com.dhemery.expressions.polling.Poller;
import com.dhemery.expressions.polling.PollingSchedule;
import org.hamcrest.Matcher;

/**
 * Expressive methods to compose conditions and evaluate them by polling.
 *
 * @see Eventually
 * @see Expressions
 * @see Poller
 */
public interface PolledExpressions extends Poller, Eventually {
    /**
     * Asserts that the supplier returns {@code true} within the schedule's
     * duration.
     *
     * @param schedule
     *         the polling interval and duration
     * @param supplier
     *         the supplier to evaluate
     *
     * @throws AssertionError
     *         if the schedule duration expires before the supplier returns
     *         {@code true}
     */
    default void assertThat(PollingSchedule schedule, SelfDescribingBooleanSupplier supplier) {
        if (poll(schedule, supplier)) return;
        throw new AssertionError(Diagnosis.of(schedule, supplier));
    }

    /**
     * Asserts that the predicate accepts the subject within the schedule's
     * duration.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         evaluates the subject
     * @param schedule
     *         the polling interval and duration
     *
     * @throws AssertionError
     *         if the schedule duration expires before the predicate accepts the
     *         subject
     */
    default <T> void assertThat(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        if (poll(schedule, subject, predicate)) return;
        throw new AssertionError(Diagnosis.of(schedule, subject, predicate));
    }

    /**
     * Asserts that the predicate accepts the value that the function derives
     * from the subject within the schedule's duration.
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
     * @param schedule
     *         the polling interval and duration
     *
     * @throws AssertionError
     *         if the schedule duration expires before the predicate accepts the
     *         value that the function derives from the subject
     */
    default <T, V> void assertThat(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate) {
        PollEvaluationResult<V> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return;
        throw new AssertionError(Diagnosis.of(schedule, subject, function, predicate, result.value()));
    }

    /**
     * Asserts that the matcher accepts the value that the function derives from
     * the subject within the schedule's duration.
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
     * @param schedule
     *         the polling interval and duration
     *
     * @throws AssertionError
     *         if the schedule duration expires before the matcher accepts the
     *         value that the function derives from the subject
     */
    default <T, V> void assertThat(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher) {
        PollEvaluationResult<V> result = poll(schedule, subject, function, matcher);
        if (result.isSatisfied()) return;
        throw new AssertionError(Diagnosis.of(schedule, subject, function, matcher, result.value()));
    }

    /**
     * Returns whether the supplier returns {@code true} within the schedule's
     * duration.
     *
     * @param schedule
     *         the polling interval and duration
     * @param supplier
     *         the supplier to evaluate
     *
     * @return {@code true} if the supplier returns {@code true} within the
     * schedule's duration, and {@code false} otherwise.
     */
    default boolean satisfiedThat(PollingSchedule schedule, SelfDescribingBooleanSupplier supplier) {
        return poll(schedule, supplier);
    }

    /**
     * Returns whether the predicate accepts the subject within the schedule's
     * duration.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         evaluates the subject
     * @param schedule
     *         the polling interval and duration
     *
     * @return {@code true} if the supplier returns {@code true} within the
     * schedule's duration, and {@code false} otherwise.
     */
    default <T> boolean satisfiedThat(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        return poll(schedule, subject, predicate);
    }

    /**
     * Returns whether the predicate accepts the value that the function derives
     * from the subject within the schedule's duration.
     *
     * @param <T>
     *         the type of the subject
     * @param <V>
     *         the type of the derived value
     * @param schedule
     *         the polling interval and duration
     * @param subject
     *         the subject to evaluate
     * @param function
     *         derives the value of interest from the subject
     * @param predicate
     *         evaluates the derived value
     *
     * @return {@code true} if the supplier returns {@code true} within the
     * schedule's duration, and {@code false} otherwise.
     */
    default <T, V> boolean satisfiedThat(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate) {
        return poll(schedule, subject, function, predicate).isSatisfied();
    }

    /**
     * Returns whether the matcher accepts the value that the function derives
     * from the subject within the schedule's duration.
     *
     * @param <T>
     *         the type of the subject
     * @param <V>
     *         the type of the derived value
     * @param schedule
     *         the polling interval and duration
     * @param subject
     *         the subject to evaluate
     * @param function
     *         derives the value of interest from the subject
     * @param matcher
     *         evaluates the derived value
     *
     * @return {@code true} if the supplier returns {@code true} within the
     * schedule's duration, and {@code false} otherwise.
     */
    default <T, V> boolean satisfiedThat(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher) {
        return poll(schedule, subject, function, matcher).isSatisfied();
    }

    /**
     * Waits until the supplier returns {@code true}.
     *
     * @param supplier
     *         the supplier to evaluate
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         supplier returns {@code true}
     */
    default void waitUntil(SelfDescribingBooleanSupplier supplier) {
        PollingSchedule schedule = eventually();
        if (poll(schedule, supplier)) return;
        throw new PollTimeoutException(schedule, supplier);
    }

    /**
     * Waits until the predicate accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         evaluates the subject
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         predicate accepts the subject
     */
    default <T> void waitUntil(T subject, SelfDescribingPredicate<? super T> predicate) {
        PollingSchedule schedule = eventually();
        if (poll(schedule, subject, predicate)) return;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Waits until the predicate accepts the value that the function derives
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
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         predicate accepts the value that the function derives from the
     *         subject
     */
    default <T, V> void waitUntil(T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate) {
        PollingSchedule schedule = eventually();
        PollEvaluationResult<V> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Waits until the matcher accepts the value that the function derives from
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
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         matcher accepts the value that the function derives from the
     *         subject
     */
    default <T, V> void waitUntil(T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher) {
        PollingSchedule schedule = eventually();
        PollEvaluationResult<V> result = poll(schedule, subject, function, matcher);
        if (result.isSatisfied()) return;
        throw new PollTimeoutException(schedule, subject, function, matcher, result.value());
    }

    /**
     * Waits until the supplier returns {@code true}.
     *
     * @param schedule
     *         the polling interval and duration
     * @param supplier
     *         the supplier to evaluate
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the supplier is
     *         satisfied
     */
    default void waitUntil(PollingSchedule schedule, SelfDescribingBooleanSupplier supplier) {
        if (poll(schedule, supplier)) return;
        throw new PollTimeoutException(schedule, supplier);
    }

    /**
     * Waits until the predicate accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param schedule
     *         the polling interval and duration
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         evaluates the subject
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the predicate accepts
     *         the subject
     */
    default <T> void waitUntil(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        if (poll(schedule, subject, predicate)) return;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Waits until the predicate accepts the value that the function derives
     * from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <V>
     *         the type of the derived value
     * @param schedule
     *         the polling interval and duration
     * @param subject
     *         the subject to evaluate
     * @param function
     *         derives the value of interest from the subject
     * @param predicate
     *         evaluates the derived value
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the predicate accepts
     *         that value that the function derives from the subject
     */
    default <T, V> void waitUntil(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate) {
        PollEvaluationResult<V> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Waits until the matcher accepts the value that the function derives from
     * the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <V>
     *         the type of the derived value
     * @param schedule
     *         the polling interval and duration
     * @param subject
     *         the subject to evaluate
     * @param function
     *         derives the value of interest from the subject
     * @param matcher
     *         evaluates the derived value
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the matcher accepts the
     *         value that the function derives from the subject
     */
    default <T, V> void waitUntil(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher) {
        PollEvaluationResult<V> result = poll(schedule, subject, function, matcher);
        if (result.isSatisfied()) return;
        throw new PollTimeoutException(schedule, subject, function, matcher, result.value());
    }

    /**
     * Returns the subject when the predicate accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         evaluates the subject
     *
     * @return the subject
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         predicate accepts the subject
     */
    default <T> T when(T subject, SelfDescribingPredicate<? super T> predicate) {
        PollingSchedule schedule = eventually();
        if (poll(schedule, subject, predicate)) return subject;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Returns the subject when the predicate accepts the value that the
     * function derives from the subject.
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
     * @return the subject
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         predicate accepts the value that the function derives from the
     *         subject
     */
    default <T, V> T when(T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate) {
        PollingSchedule schedule = eventually();
        PollEvaluationResult<V> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Returns the subject when the matcher accepts the value that the function
     * derives from the subject.
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
     * @return the subject
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the
     *         matcher accepts the value that the function derives from the
     *         subject
     */
    default <T, V> T when(T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher) {
        PollingSchedule schedule = eventually();
        PollEvaluationResult<V> result = poll(schedule, subject, function, matcher);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, matcher, result.value());
    }

    /**
     * Returns the subject when the predicate accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param schedule
     *         the polling interval and duration
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         evaluates the subject
     *
     * @return the subject
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the predicate accepts
     *         the subject
     */
    default <T> T when(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        if (poll(schedule, subject, predicate)) return subject;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Returns the subject when the predicate accepts the value that the
     * function derives from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <V>
     *         the type of the derived value
     * @param schedule
     *         the polling interval and duration
     * @param subject
     *         the subject to evaluate
     * @param function
     *         derives the value of interest from the subject
     * @param predicate
     *         evaluates the derived value
     *
     * @return the subject
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the predicate accepts
     *         the value that the function derives from the subject
     */
    default <T, V> T when(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate) {
        PollEvaluationResult<V> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Returns the subject when the matcher accepts the value that the function
     * derives from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <V>
     *         the type of the derived value
     * @param schedule
     *         the polling interval and duration
     * @param subject
     *         the subject to evaluate
     * @param function
     *         derives the value of interest from the subject
     * @param matcher
     *         evaluates the derived value
     *
     * @return the subject
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the matcher accepts the
     *         value that the function derives from the subject
     */
    default <T, V> T when(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher) {
        PollEvaluationResult<V> result = poll(schedule, subject, function, matcher);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, matcher, result.value());
    }
}
