package com.dhemery.expressions.polling;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class ClockPollTimer implements PollTimer {
    private final Clock clock;
    private final Sleeper sleeper;
    private Duration interval;
    private Instant wakeupTime;
    private Instant expiration;

    public ClockPollTimer(Clock clock, Sleeper sleeper) {
        this.clock = clock;
        this.sleeper = sleeper;
    }

    @Override
    public void start(PollingSchedule schedule) {
        interval = schedule.interval();
        wakeupTime = clock.instant();
        expiration = wakeupTime.plus(schedule.duration());
    }

    @Override
    public boolean isExpired() {
        return clock.instant().compareTo(expiration) >= 0;
    }

    @Override
    public void tick() {
        Instant tickStartTime = clock.instant();
        while (wakeupTime.compareTo(tickStartTime) <= 0) {
            wakeupTime = wakeupTime.plus(interval);
        }
        Duration sleepDuration = Duration.between(tickStartTime, wakeupTime);
        sleeper.sleep(sleepDuration);
    }
}
