package com.dhemery.express;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Expressive methods to poll composed conditions.
 *
 * @see Expressions
 * @see Poller
 */
public interface PolledExpressions extends Poller, Eventually {
    /**
     * Assert that the condition is satisfied within the schedule's duration.
     *
     * @param schedule
     *         the schedule that governs the polling
     * @param condition
     *         the condition to satisfy
     */
    default <C extends SelfDescribing & BooleanSupplier>
    void assertThat(PollingSchedule schedule, C condition) {
        if (poll(schedule, condition)) return;
        throw new AssertionError(Diagnosis.of(schedule, condition));
    }

    /**
     * Assert that the predicate accepts the subject within the schedule's duration.
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
    default <T, P extends SelfDescribing & Predicate<? super T>>
    void assertThat(PollingSchedule schedule, T subject, P predicate) {
        if (poll(schedule, subject, predicate)) return;
        throw new AssertionError(Diagnosis.of(schedule, subject, predicate));
    }

    /**
     * Assert that the predicate accepts the value that the function derives from the subject within the schedule's
     * duration.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param predicate
     *         the predicate that evaluates the derived value
     * @param schedule
     *         the schedule that governs the polling
     */
    default <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
    void assertThat(PollingSchedule schedule, T subject, F function, P predicate) {
        PollEvaluationResult<R> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return;
        throw new AssertionError(Diagnosis.of(schedule, subject, function, predicate, result.value()));
    }

    /**
     * Assert that the matcher accepts the value that the function derives from the subject within the schedule's
     * duration.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param matcher
     *         the matcher that evaluates the derived value
     * @param schedule
     *         the schedule that governs the polling
     */
    default <T, R, F extends SelfDescribing & Function<? super T, R>>
    void assertThat(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
        PollEvaluationResult<R> result = poll(schedule, subject, function, matcher);
        if (result.isSatisfied()) return;
        throw new AssertionError(Diagnosis.of(schedule, subject, function, matcher, result.value()));
    }

    /**
     * Indicate whether the condition is satisfied within the schedule's duration.
     *
     * @param schedule
     *         the schedule that governs the polling
     * @param condition
     *         the condition to satisfy
     *
     * @return {@code true} if the condition is satisfied with the schedule's duration, and {@code false} otherwise.
     */
    default <C extends SelfDescribing & BooleanSupplier>
    boolean satisfiedThat(PollingSchedule schedule, C condition) {
        return poll(schedule, condition);
    }

    /**
     * Indicate whether the predicate accepts the subject within the schedule's duration.
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
     * @return {@code true} if the condition is satisfied within the schedule's duration, and {@code false} otherwise.
     */
    default <T, P extends SelfDescribing & Predicate<? super T>>
    boolean satisfiedThat(PollingSchedule schedule, T subject, P predicate) {
        return poll(schedule, subject, predicate);
    }

    /**
     * Indicate whether the predicate accepts the value that the function derives from the subject within the schedule's
     * duration.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     * @param schedule
     *         the schedule that governs the polling
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param predicate
     *         the predicate that evaluates the derived value
     *
     * @return {@code true} if the condition is satisfied within the schedule's duration, and {@code false} otherwise.
     */
    default <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
    boolean satisfiedThat(PollingSchedule schedule, T subject, F function, P predicate) {
        return poll(schedule, subject, function, predicate).isSatisfied();
    }

    /**
     * Indicate whether the matcher accepts the value that the function derives from the subject within the schedule's
     * duration.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     * @param schedule
     *         the schedule that governs the polling
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param matcher
     *         the matcher that evaluates the derived value
     *
     * @return {@code true} if the condition is satisfied within the schedule's duration, and {@code false} otherwise.
     */
    default <T, R, F extends SelfDescribing & Function<? super T, R>>
    boolean satisfiedThat(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
        return poll(schedule, subject, function, matcher).isSatisfied();
    }

    /**
     * Wait until the condition is satisfied.
     *
     * @param condition
     *         the condition to satisfy
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the condition is satisfied
     */
    default <C extends SelfDescribing & BooleanSupplier>
    void waitUntil(C condition) {
        PollingSchedule schedule = eventually();
        if (poll(schedule, condition)) return;
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
     *         if the default polling schedule's duration expires before the condition is satisfied
     */
    default <T, P extends SelfDescribing & Predicate<? super T>>
    void waitUntil(T subject, P predicate) {
        PollingSchedule schedule = eventually();
        if (poll(schedule, subject, predicate)) return;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Wait until the predicate accepts the value that the function derives from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param predicate
     *         the predicate that evaluates the derived value
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the condition is satisfied
     */
    default <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
    void waitUntil(T subject, F function, P predicate) {
        PollingSchedule schedule = eventually();
        PollEvaluationResult<R> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Wait until the matcher accepts the value that the function derives from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param matcher
     *         the matcher that evaluates the derived value
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the condition is satisfied
     */
    default <T, R, F extends SelfDescribing & Function<? super T, R>>
    void waitUntil(T subject, F function, Matcher<? super R> matcher) {
        PollingSchedule schedule = eventually();
        PollEvaluationResult<R> result = poll(schedule, subject, function, matcher);
        if (result.isSatisfied()) return;
        throw new PollTimeoutException(schedule, subject, function, matcher, result.value());
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
     *         if the schedule's duration expires before the condition is satisfied
     */
    default <C extends SelfDescribing & BooleanSupplier>
    void waitUntil(PollingSchedule schedule, C condition) {
        if (poll(schedule, condition)) return;
        throw new PollTimeoutException(schedule, condition);
    }

    /**
     * Wait until the predicate accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param schedule
     *         the schedule that governs the polling
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         the predicate that evaluates the subject
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is satisfied
     */
    default <T, P extends SelfDescribing & Predicate<? super T>>
    void waitUntil(PollingSchedule schedule, T subject, P predicate) {
        if (poll(schedule, subject, predicate)) return;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Wait until the predicate accepts the value that the function derives from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     * @param schedule
     *         the schedule that governs the polling
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param predicate
     *         the predicate that evaluates the derived value
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is satisfied
     */
    default <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
    void waitUntil(PollingSchedule schedule, T subject, F function, P predicate) {
        PollEvaluationResult<R> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Wait until the matcher accepts the value that the function derives from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     * @param schedule
     *         the schedule that governs the polling
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param matcher
     *         the matcher that evaluates the derived value
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is satisfied
     */
    default <T, R, F extends SelfDescribing & Function<? super T, R>>
    void waitUntil(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
        PollEvaluationResult<R> result = poll(schedule, subject, function, matcher);
        if (result.isSatisfied()) return;
        throw new PollTimeoutException(schedule, subject, function, matcher, result.value());
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
     *         if the default polling schedule's duration expires before the condition is satisfied
     */
    default <T, R, P extends SelfDescribing & Predicate<? super T>>
    T when(T subject, P predicate) {
        PollingSchedule schedule = eventually();
        if (poll(schedule, subject, predicate)) return subject;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Return the subject when the predicate accepts the value that the function derives from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param predicate
     *         the predicate that evaluates the derived value
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the condition is satisfied
     */
    default <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
    T when(T subject, F function, P predicate) {
        PollingSchedule schedule = eventually();
        PollEvaluationResult<R> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Return the subject when the matcher accepts the value that the function derives from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param matcher
     *         the matcher that evaluates the derived value
     *
     * @throws PollTimeoutException
     *         if the default polling schedule's duration expires before the condition is satisfied
     */
    default <T, R, F extends SelfDescribing & Function<? super T, R>>
    T when(T subject, F function, Matcher<? super R> matcher) {
        PollingSchedule schedule = eventually();
        PollEvaluationResult<R> result = poll(schedule, subject, function, matcher);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, matcher, result.value());
    }

    /**
     * Return the subject when the predicate accepts the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param schedule
     *         the schedule that governs the polling
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         the predicate that evaluates the subject
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is satisfied
     */
    default <T, P extends SelfDescribing & Predicate<? super T>>
    T when(PollingSchedule schedule, T subject, P predicate) {
        if (poll(schedule, subject, predicate)) return subject;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Return the subject when the predicate accepts the value that the function derives from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     * @param schedule
     *         the schedule that governs the polling
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param predicate
     *         the predicate that evaluates the derived value
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is satisfied
     */
    default <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
    T when(PollingSchedule schedule, T subject, F function, P predicate) {
        PollEvaluationResult<R> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Return the subject when the matcher accepts the value that the function derives from the subject.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     * @param schedule
     *         the schedule that governs the polling
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param matcher
     *         the matcher that evaluates the derived value
     *
     * @throws PollTimeoutException
     *         if the schedule's duration expires before the condition is satisfied
     */
    default <T, R, F extends SelfDescribing & Function<? super T, R>>
    T when(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher) {
        PollEvaluationResult<R> result = poll(schedule, subject, function, matcher);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, matcher, result.value());
    }
}
