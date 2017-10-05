package com.dhemery.expressions.helpers;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

public class ManualClock extends Clock {
    private Instant now;

    public ManualClock(Instant startTime) {
        now = startTime;
    }

    public ManualClock() {
        this(Instant.now());
    }

    @Override
    public ZoneId getZone() {
        return null;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return null;
    }

    @Override
    public Instant instant() {
        return now;
    }

    public void advance(Duration amountToAdvance) {
        now = now.plus(amountToAdvance);
    }
}
