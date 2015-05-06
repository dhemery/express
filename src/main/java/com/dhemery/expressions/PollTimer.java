package com.dhemery.expressions;

public interface PollTimer {
    void start(PollingSchedule schedule);

    void tick();

    boolean isExpired();
}
