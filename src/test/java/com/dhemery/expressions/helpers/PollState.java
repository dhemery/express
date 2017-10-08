package com.dhemery.expressions.helpers;

public class PollState {
    private boolean started;

    public void start() {
        this.started = true;
    }

    public boolean isStarted() {
        return started;
    }
}
