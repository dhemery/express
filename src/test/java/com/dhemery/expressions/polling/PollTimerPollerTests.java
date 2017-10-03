package com.dhemery.expressions.polling;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class PollTimerPollerTests {
    @Disabled("needs new implementation")
    @Test
    void startsTimerWithSchedule_beforeCheckingForExpiration() {
    }

    @Disabled("needs new implementation")
    @Test
    void returnsFalse_withoutEvaluatingSupplier_ifTimerIsAlreadyExpiredAtStartOfPoll() {
    }

    @Disabled("needs new implementation")
    @Test
    void returnsTrue_ifSupplierReturnsTrue_beforeTimerExpires() {
    }

    @Disabled("needs new implementation")
    @Test
    void returnsFalse_ifTimerExpires_beforeSupplierReturnsTrue() {
    }

    @Disabled("needs new implementation")
    @Test
    void ticksTimer_betweenEvaluations() {
    }
}
