package com.dhemery.express;

public class PollSample<T> {
    private final T value;
    private final boolean satisfied;

    public PollSample(T value, boolean satisfied){
        this.value = value;
        this.satisfied = satisfied;
    }

    T value() { return value; }
    boolean isSatisfied() { return satisfied; }
}
