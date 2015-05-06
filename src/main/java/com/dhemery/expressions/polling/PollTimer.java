package com.dhemery.expressions.polling;

import com.dhemery.expressions.polling.PollingSchedule;

public interface PollTimer {
    void start(PollingSchedule schedule);

    void tick();

    boolean isExpired();
}
