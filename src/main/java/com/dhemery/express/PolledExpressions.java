package com.dhemery.express;

import org.hamcrest.Matcher;

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
    default void assertThat(PollingSchedule schedule, SelfDescribingBooleanSupplier condition) {
        if (poll(schedule, condition)) return;
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
    default <T> void assertThat(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        if (poll(schedule, subject, predicate)) return;
        throw new AssertionError(Diagnosis.of(schedule, subject, predicate));
    }

    /**
     * Assert that the predicate accepts the value that the function derives
     * from the subject within the schedule's duration.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the derived value
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param predicate
     *         the predicate that evaluates the derived value
     * @param schedule
     *         the schedule that governs the polling
     */
    default <T, R> void assertThat(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, R> function, SelfDescribingPredicate<? super R> predicate) {
        PollEvaluationResult<R> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return;
        throw new AssertionError(Diagnosis.of(schedule, subject, function, predicate, result.value()));
    }

    /**
     * Assert that the matcher accepts the value that the function derives from
     * the subject within the schedule's duration.
     *
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the derived value
     * @param subject
     *         the subject to evaluate
     * @param function
     *         the function that derives the value of interest
     * @param matcher
     *         the matcher that evaluates the derived value
     * @param schedule
     *         the schedule that governs the polling
     */
    default <T, R> void assertThat(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, R> function, Matcher<? super R> matcher) {
        PollEvaluationResult<R> result = poll(schedule, subject, function, matcher);
        if (result.isSatisfied()) return;
        throw new AssertionError(Diagnosis.of(schedule, subject, function, matcher, result.value()));
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
    default boolean satisfiedThat(PollingSchedule schedule, SelfDescribingBooleanSupplier condition) {
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
    default <T> boolean satisfiedThat(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        return poll(schedule, subject, predicate);
    }

    /**
     * Indicate whether the predicate accepts the value that the function
     * derives from the subject within the schedule's duration.
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
     * @return {@code true} if the condition is satisfied within the schedule's
     * duration, and {@code false} otherwise.
     */
    default <T, R> boolean satisfiedThat(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, R> function, SelfDescribingPredicate<? super R> predicate) {
        return poll(schedule, subject, function, predicate).isSatisfied();
    }

    /**
     * Indicate whether the matcher accepts the value that the function derives
     * from the subject within the schedule's duration.
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
     * @return {@code true} if the condition is satisfied within the schedule's
     * duration, and {@code false} otherwise.
     */
    default <T, R> boolean satisfiedThat(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, R> function, Matcher<? super R> matcher) {
        return poll(schedule, subject, function, matcher).isSatisfied();
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
    default void waitUntil(SelfDescribingBooleanSupplier condition) {
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
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default <T> void waitUntil(T subject, SelfDescribingPredicate<? super T> predicate) {
        PollingSchedule schedule = eventually();
        if (poll(schedule, subject, predicate)) return;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Wait until the predicate accepts the value that the function derives from
     * the subject.
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
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default <T, R> void waitUntil(T subject, SelfDescribingFunction<? super T, R> function, SelfDescribingPredicate<? super R> predicate) {
        PollingSchedule schedule = eventually();
        PollEvaluationResult<R> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Wait until the matcher accepts the value that the function derives from
     * the subject.
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
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default <T, R> void waitUntil(T subject, SelfDescribingFunction<? super T, R> function, Matcher<? super R> matcher) {
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
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default void waitUntil(PollingSchedule schedule, SelfDescribingBooleanSupplier condition) {
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
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default <T> void waitUntil(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        if (poll(schedule, subject, predicate)) return;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Wait until the predicate accepts the value that the function derives from
     * the subject.
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
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default <T, R> void waitUntil(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, R> function, SelfDescribingPredicate<? super R> predicate) {
        PollEvaluationResult<R> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Wait until the matcher accepts the value that the function derives from
     * the subject.
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
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default <T, R> void waitUntil(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, R> function, Matcher<? super R> matcher) {
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
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default <T> T when(T subject, SelfDescribingPredicate<? super T> predicate) {
        PollingSchedule schedule = eventually();
        if (poll(schedule, subject, predicate)) return subject;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Return the subject when the predicate accepts the value that the function
     * derives from the subject.
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
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default <T, R> T when(T subject, SelfDescribingFunction<? super T, R> function, SelfDescribingPredicate<? super R> predicate) {
        PollingSchedule schedule = eventually();
        PollEvaluationResult<R> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Return the subject when the matcher accepts the value that the function
     * derives from the subject.
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
     *         if the default polling schedule's duration expires before the
     *         condition is satisfied
     */
    default <T, R> T when(T subject, SelfDescribingFunction<? super T, R> function, Matcher<? super R> matcher) {
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
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default <T> T when(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        if (poll(schedule, subject, predicate)) return subject;
        throw new PollTimeoutException(schedule, subject, predicate);
    }

    /**
     * Return the subject when the predicate accepts the value that the function
     * derives from the subject.
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
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default <T, R> T when(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, R> function, SelfDescribingPredicate<? super R> predicate) {
        PollEvaluationResult<R> result = poll(schedule, subject, function, predicate);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, predicate, result.value());
    }

    /**
     * Return the subject when the matcher accepts the value that the function
     * derives from the subject.
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
     *         if the schedule's duration expires before the condition is
     *         satisfied
     */
    default <T, R> T when(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, R> function, Matcher<? super R> matcher) {
        PollEvaluationResult<R> result = poll(schedule, subject, function, matcher);
        if (result.isSatisfied()) return subject;
        throw new PollTimeoutException(schedule, subject, function, matcher, result.value());
    }
}
