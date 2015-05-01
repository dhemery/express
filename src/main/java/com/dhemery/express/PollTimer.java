package com.dhemery.express;

public interface PollTimer {
    void start(PollingSchedule schedule);
    void tick();

    boolean isExpired();
}
