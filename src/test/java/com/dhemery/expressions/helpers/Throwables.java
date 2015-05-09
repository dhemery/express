package com.dhemery.expressions.helpers;

public class Throwables {
    public static Throwable throwableThrownBy(Runnable expression) {
        try {
            expression.run();
        } catch (Throwable thrown) {
            return thrown;
        }
        throw new AssertionError("The assertion did not throw anything");
    }

    public static String[] linesOfMessageThrownBy(Runnable runnable) {
        return messageThrownBy(runnable).split(System.lineSeparator());
    }

    public static String messageThrownBy(Runnable runnable) {
        //noinspection ThrowableResultOfMethodCallIgnored
        return throwableThrownBy(runnable).getMessage();
    }
}