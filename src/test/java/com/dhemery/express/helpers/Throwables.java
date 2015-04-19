package com.dhemery.express.helpers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Optional;

public class Throwables {
    public static Optional<Throwable> throwableThrownByRunning(Runnable expression) {
        try {
            expression.run();
            return Optional.empty();
        } catch (AssertionError thrown1) {
            return Optional.of(thrown1);
        }
    }

    public static Matcher<? super Optional<?>> present() {
        return new TypeSafeMatcher<Optional<?>>() {
            @Override
            protected boolean matchesSafely(Optional<?> item) {
                return item.isPresent();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("present");
            }
        };
    }
}
