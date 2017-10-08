package com.dhemery.expressions.polling;

import com.dhemery.expressions.Poller;
import com.dhemery.expressions.helpers.PollState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComposedPollerTests {
    @Test
    void startsBeforeEvaluating() {
        PollState state = new PollState();
        Poller poller = new ComposedPoller(state::start, null, null);
        assertTrue(poller.poll(state::isStarted));
    }
}
