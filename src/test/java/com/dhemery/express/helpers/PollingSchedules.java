package com.dhemery.express.helpers;

import com.dhemery.express.PollingSchedule;

import java.time.Duration;
import java.util.Random;

public class PollingSchedules {
    public static PollingSchedule random() {
        Random random = new Random();
        Duration interval = Duration.ofSeconds(random.nextInt(60));
        Duration duration = Duration.ofMinutes(random.nextInt(60));
        return new PollingSchedule(interval, duration);
    }
}
