package com.dhemery.expressions.polling;

public interface PollTimer {
    void start(PollingSchedule schedule);

    void tick();

    boolean isExpired();
}
