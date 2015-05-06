package com.dhemery.expressions.polling;

import com.dhemery.expressions.SelfDescribingBooleanSupplier;
import com.dhemery.expressions.SelfDescribingFunction;
import com.dhemery.expressions.SelfDescribingPredicate;
import com.dhemery.expressions.polling.PollEvaluationResult;
import com.dhemery.expressions.polling.PollingSchedule;
import org.hamcrest.Matcher;

/**
 * Poll a condition on a schedule until either the condition is satisfied or the
 * schedule expires.
 */
public interface Poller {
    /**
     * Polls the supplier.
     *
     * @param schedule
     *         the polling interval and duration
     * @param supplier
     *         the supplier to evaluate
     *
     * @return {@code true} if the supplier returns {@code true} before the
     * schedule expires, otherwise {@code false}
     */
    boolean poll(PollingSchedule schedule, SelfDescribingBooleanSupplier supplier);

    /**
     * Polls the predicate's acceptance of the subject.
     *
     * @param schedule
     *         the polling interval and duration
     * @param subject
     *         the subject to evaluate
     * @param predicate
     *         evaluates the subject
     * @param <T>
     *         the type of the subject
     *
     * @return {@code true} if the predicate accepts the subject before the
     * schedule expires, otherwise {@code false}
     */
    <T> boolean poll(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate);

    /**
     * Polls the matcher's acceptance of the value that the function derives
     * from the subject.
     * <p>
     * The return value includes the value derived by the function during the
     * final evaluation, and indicates whether that value satisfied the
     * matcher.
     *
     * @param schedule
     *         the polling interval and duration
     * @param subject
     *         the subject to evaluate
     * @param function
     *         derives the value of interest from the subject
     * @param matcher
     *         evaluates the derived value
     * @param <T>
     *         the type of the subject
     * @param <V>
     *         the type of the derived value
     *
     * @return the result of the final evaluation performed by this poll
     */
    <T, V> PollEvaluationResult<V> poll(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher);

    /**
     * Polls the matcher's acceptance of the value that the function derives
     * from the subject.
     * <p>
     * The return value includes the value derived by the function during the
     * final evaluation, and indicates whether that value satisfied the
     * predicate.
     *
     * @param schedule
     *         the polling interval and duration
     * @param subject
     *         the subject to evaluate
     * @param function
     *         derives the value of interest from the subject
     * @param predicate
     *         evaluates the derived value
     * @param <T>
     *         the type of the subject
     * @param <V>
     *         the type of the derived value
     *
     * @return the result of the final evaluation performed by this poll
     */
    <T, V> PollEvaluationResult<V> poll(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate);
}
