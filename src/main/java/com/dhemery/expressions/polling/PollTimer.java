package com.dhemery.expressions.polling;

import com.dhemery.expressions.PollingSchedule;

/**
 * Manages time for a single poll.
 */
public interface PollTimer {
    /**
     * Marks the beginning of a poll using the given schedule.
     *
     * @param schedule
     *         the interval and duration for the poll
     */
    void start(PollingSchedule schedule);

    /**
     * Pauses execution until the next evaluation time arrives.
     */
    void tick();

    /**
     * Returns whether the polling duration has passed since the timer was started.
     *
     * @return whether the polling duration has passed since the timer was started
     */
    boolean isExpired();
}
