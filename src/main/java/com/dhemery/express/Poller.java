package com.dhemery.express;

import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Poll a condition on a schedule until either the condition is satisfied or the schedule expires.
 */
public interface Poller {
    /**
     * Polls the supplier. Repeatedly evaluates the supplier on the given schedule until either the supplier returns
     * {@code true} or the schedule expires.
     *
     * @param schedule
     *         the schedule on which to poll
     * @param supplier
     *         the supplier to evaluate
     *
     * @return {@code true} if the supplier returns {@code true} before the schedule expires, otherwise {@code false}
     */
    <C extends SelfDescribing & BooleanSupplier>
    boolean poll(PollingSchedule schedule, C supplier);

    /**
     * Polls the predicate's acceptance of the subject. Repeatedly evaluates the predicate's acceptance of the subject,
     * on the given schedule, until either the predicate accepts the subject or the schedule expires.
     *
     * @param schedule
     *         the schedule on which to poll
     * @param subject
     *         the subject of the poll
     * @param predicate
     *         evaluates the subject
     * @param <T>
     *         the type of the subject
     *
     * @return {@code true} if the subject satisfies the predicate before the schedule expires, otherwise {@code false}
     */
    <T, P extends SelfDescribing & Predicate<? super T>>
    boolean poll(PollingSchedule schedule, T subject, P predicate);

    /**
     * Polls the matcher's acceptance of the value that the function derives from the subject. Repeatedly evaluates the
     * matcher's acceptance of the value that the function derives from the subject, on the given schedule, until either
     * the matcher accepts the derived value or the schedule expires.
     * <p>
     * The return value includes the value derived by the function during the final evaluation, and indicates whether
     * that value satisfied the matcher.
     *
     * @param schedule
     *         the schedule on which to poll
     * @param subject
     *         the subject of the poll
     * @param function
     *         derives the value of interest from the subject
     * @param matcher
     *         evaluates the derived value
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     *
     * @return the result of the final evaluation performed by this poll
     */
    <T, R, F extends SelfDescribing & Function<? super T, R>>
    PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, Matcher<? super R> matcher);

    /**
     * Polls the matcher's acceptance of the value that the function derives from the subject. Repeatedly evaluates the
     * predicate's acceptance of the value that the function derives from the subject, on the given schedule, until
     * either the predicate accepts the derived value or the schedule expires.
     * <p>
     * The return value includes the value derived by the function during the final evaluation, and indicates whether
     * that value satisfied the predicate.
     *
     * @param schedule
     *         the schedule on which to poll
     * @param subject
     *         the subject of the poll
     * @param function
     *         derives the value of interest from the subject
     * @param predicate
     *         evaluates the derived value
     * @param <T>
     *         the type of the subject
     * @param <R>
     *         the type of the result of the function
     *
     * @return the result of the final evaluation performed by this poll
     */
    <T, R, F extends SelfDescribing & Function<? super T, R>, P extends SelfDescribing & Predicate<? super R>>
    PollEvaluationResult<R> poll(PollingSchedule schedule, T subject, F function, P predicate);
}
