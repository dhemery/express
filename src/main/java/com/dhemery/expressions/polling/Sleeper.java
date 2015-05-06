package com.dhemery.expressions.polling;

import java.time.Duration;

public interface Sleeper {
    void sleep(Duration duration);
}
