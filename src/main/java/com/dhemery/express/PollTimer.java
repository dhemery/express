package com.dhemery.express;

public interface PollTimer {
    void start();
    boolean isExpired();

    void tick();
}
