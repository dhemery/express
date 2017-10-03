package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.Diagnosis;
import com.dhemery.expressions.polling.PollEvaluationResult;
import com.dhemery.expressions.polling.PollTimeoutException;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Methods to compose conditions, evaluate them by polling, and act on the
 * results.
 *
 * @see Expressions
 * @see Poller
 * @see PollingSchedule
 */
public interface PolledExpressions {
    Poller poller();
    PollingSchedule eventually();

    /**
     * Asserts that the supplier returns {@code true} within the schedule's
     * duration.
     *
     * @param schedule the polling interval and duration
     * @param supplier the supplier to evaluate
     * @throws AssertionError if the schedule duration expires before the supplier returns
     *                        {@code true}
     */
    default void assertThat(PollingSchedule schedule, BooleanSupplier supplier) {
        if (poller().poll(schedule, supplier)) return;
        throw new AssertionError(Diagnosis.of(schedule, supplier));
    }


    /**
     * Asserts that the predicate accepts the subject within the schedule's
     * duration.
     *
     * @param <T>       the type of the subject
     * @param subject   the subject to evaluate
     * @param predicate evaluates the subject
     * @param schedule  the polling interval and duration
     * @throws AssertionError if the schedule duration expires before the predicate accepts the
     *                        subject
     */
    default <T> void assertThat(PollingSchedule schedule, T subject, Predicate<? super T> predicate) {
        if (poller().poll(schedule, subject, predicate)) return;
        throw new AssertionError(Diagnosis.of(schedule, subject, predicate));
    }

    /**
     * Asserts that the predicate accepts the value that the function derives
     * from the subject within the schedule's duration.
     *
     * @param <T>       the type of the subject
     * @param <V>       the type of the derived value
     * @param subject   the subject to evaluate
     * @param function  derives the value of interest from the subject
     * @param predicate evaluates the derived value
     * @param schedule  the polling interval and duration
     * @throws AssertionError if the schedule duration expires before the predicate accepts the
     *                        value that the function derives from the subject
     */
    default <T, V> void assertThat(PollingSchedule schedule, T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        PollEvaluationResult<V> result = poller().poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return;
        throw new AssertionError(Diagnosis.of(schedule, subject, function, predicate, result.value()));
    }


    /**
     * Evaluates whether the supplier returns {@code true} within the schedule's
     * duration.
     *
     * @param schedule the polling interval and duration
     * @param supplier the supplier to evaluate
     * @return {@code true} if the supplier returns {@code true} within the
     * schedule's duration, and {@code false} otherwise.
     */
    default boolean satisfiedThat(PollingSchedule schedule, BooleanSupplier supplier) {
        return poller().poll(schedule, supplier);
    }

    /**
     * Evaluates whether the predicate accepts the subject within the schedule's
     * duration.
     *
     * @param <T>       the type of the subject
     * @param subject   the subject to evaluate
     * @param predicate evaluates the subject
     * @param schedule  the polling interval and duration
     * @return {@code true} if the supplier returns {@code true} within the
     * schedule's duration, and {@code false} otherwise.
     */
    default <T> boolean satisfiedThat(PollingSchedule schedule, T subject, Predicate<? super T> predicate) {
        return poller().poll(schedule, subject, predicate);
    }

    /**
     * Evaluates whether the predicate accepts the value that the function derives
     * from the subject within the schedule's duration.
     *
     * @param <T>       the type of the subject
     * @param <V>       the type of the derived value
     * @param schedule  the polling interval and duration
     * @param subject   the subject to evaluate
     * @param function  derives the value of interest from the subject
     * @param predicate evaluates the derived value
     * @return {@code true} if the supplier returns {@code true} within the
     * schedule's duration, and {@code false} otherwise.
     */
    default <T, V> boolean satisfiedThat(PollingSchedule schedule, T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        return poller().poll(schedule, subject, function, predicate).isSatisfied();
    }


    /**
     * Waits until the supplier returns {@code true}.
     *
     * @param supplier the supplier to evaluate
     * @throws PollTimeoutException if the default polling schedule's duration expires before the
     *                              supplier returns {@code true}
     */
    default void waitUntil(BooleanSupplier supplier) {
        PollingSchedule schedule = eventually();
        if (poller().poll(schedule, supplier)) return;
        throw new PollTimeoutException(schedule, supplier);
    }

    /**
     * Waits until the predicate accepts the subject.
     *
     * @param <T>       the type of the subject
     * @param subject   the subject to evaluate
     * @param predicate evaluates the subject
     * @throws PollTimeoutException if the default polling schedule's duration expires before the
     *                              predicate accepts the subject
     */
    default <T> void waitUntil(T subject, Predicate<? super T> predicate) {
        PollingSchedule schedule = eventually();
        if (poller().poll(schedule, subject, predicate)) return;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Waits until the predicate accepts the value that the function derives
     * from the subject.
     *
     * @param <T>       the type of the subject
     * @param <V>       the type of the derived value
     * @param subject   the subject to evaluate
     * @param function  derives the value of interest from the subject
     * @param predicate evaluates the derived value
     * @throws PollTimeoutException if the default polling schedule's duration expires before the
     *                              predicate accepts the value that the function derives from the
     *                              subject
     */
    default <T, V> void waitUntil(T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        PollingSchedule schedule = eventually();
        PollEvaluationResult<V> result = poller().poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }


    /**
     * Waits until the supplier returns {@code true}.
     *
     * @param schedule the polling interval and duration
     * @param supplier the supplier to evaluate
     * @throws PollTimeoutException if the schedule's duration expires before the supplier is
     *                              satisfied
     */
    default void waitUntil(PollingSchedule schedule, BooleanSupplier supplier) {
        if (poller().poll(schedule, supplier)) return;
        throw new PollTimeoutException(schedule, supplier);
    }

    /**
     * Waits until the predicate accepts the subject.
     *
     * @param <T>       the type of the subject
     * @param schedule  the polling interval and duration
     * @param subject   the subject to evaluate
     * @param predicate evaluates the subject
     * @throws PollTimeoutException if the schedule's duration expires before the predicate accepts
     *                              the subject
     */
    default <T> void waitUntil(PollingSchedule schedule, T subject, Predicate<? super T> predicate) {
        if (poller().poll(schedule, subject, predicate)) return;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Waits until the predicate accepts the value that the function derives
     * from the subject.
     *
     * @param <T>       the type of the subject
     * @param <V>       the type of the derived value
     * @param schedule  the polling interval and duration
     * @param subject   the subject to evaluate
     * @param function  derives the value of interest from the subject
     * @param predicate evaluates the derived value
     * @throws PollTimeoutException if the schedule's duration expires before the predicate accepts
     *                              that value that the function derives from the subject
     */
    default <T, V> void waitUntil(PollingSchedule schedule, T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        PollEvaluationResult<V> result = poller().poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Returns the subject when the predicate accepts the subject.
     *
     * @param <T>       the type of the subject
     * @param subject   the subject to evaluate
     * @param predicate evaluates the subject
     * @return the subject
     * @throws PollTimeoutException if the default polling schedule's duration expires before the
     *                              predicate accepts the subject
     */
    default <T> T when(T subject, Predicate<? super T> predicate) {
        PollingSchedule schedule = eventually();
        if (poller().poll(schedule, subject, predicate)) return subject;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Returns the subject when the predicate accepts the value that the
     * function derives from the subject.
     *
     * @param <T>       the type of the subject
     * @param <V>       the type of the derived value
     * @param subject   the subject to evaluate
     * @param function  derives the value of interest from the subject
     * @param predicate evaluates the derived value
     * @return the subject
     * @throws PollTimeoutException if the default polling schedule's duration expires before the
     *                              predicate accepts the value that the function derives from the
     *                              subject
     */
    default <T, V> T when(T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        PollingSchedule schedule = eventually();
        PollEvaluationResult<V> result = poller().poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Returns the subject when the predicate accepts the subject.
     *
     * @param <T>       the type of the subject
     * @param schedule  the polling interval and duration
     * @param subject   the subject to evaluate
     * @param predicate evaluates the subject
     * @return the subject
     * @throws PollTimeoutException if the schedule's duration expires before the predicate accepts
     *                              the subject
     */
    default <T> T when(PollingSchedule schedule, T subject, Predicate<? super T> predicate) {
        if (poller().poll(schedule, subject, predicate)) return subject;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Returns the subject when the predicate accepts the value that the
     * function derives from the subject.
     *
     * @param <T>       the type of the subject
     * @param <V>       the type of the derived value
     * @param schedule  the polling interval and duration
     * @param subject   the subject to evaluate
     * @param function  derives the value of interest from the subject
     * @param predicate evaluates the derived value
     * @return the subject
     * @throws PollTimeoutException if the schedule's duration expires before the predicate accepts
     *                              the value that the function derives from the subject
     */
    default <T, V> T when(PollingSchedule schedule, T subject, Function<? super T, V> function, Predicate<? super V> predicate) {
        PollEvaluationResult<V> result = poller().poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }
}
