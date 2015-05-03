package com.dhemery.express;

/**
 * The result of a single evaluation during a poll.
 *
 * @param <T>
 *         the type of value evaluated
 */
public class PollEvaluationResult<T> {
    private final T value;
    private final boolean satisfied;

    /**
     * Creates a poll evaluation result indicating whether the given value satisfied the poll criteria.
     *
     * @param value
     *         the value evaluated by the poll evaluation
     * @param satisfied
     *         indicates whether the value satisfied the poll criteria
     */
    public PollEvaluationResult(T value, boolean satisfied) {
        this.value = value;
        this.satisfied = satisfied;
    }

    /**
     * Returns the value evaluated by the poll evaluation.
     *
     * @return the value evaluated by the poll evaluation
     */
    T value() {
        return value;
    }

    /**
     * Indicates whether the value satisfied the poll criteria
     *
     * @return {@code true} if the value satisfied the poll criteria, otherwise {@code false}
     */
    boolean isSatisfied() {
        return satisfied;
    }
}
