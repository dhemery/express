package com.dhemery.express.helpers;

public class Throwables {
    public static AssertionError assertionErrorThrownBy(Runnable expression) {
        try {
            expression.run();
        } catch (AssertionError thrown) {
            return thrown;
        }
        throw new AssertionError("The assertion did not throw an AssertionError");
    }

    public static String[] messageOfAssertionErrorThrownBy(Runnable runnable) {
        AssertionError thrown = assertionErrorThrownBy(runnable);
        String message = thrown.getMessage();
        return message.split(System.lineSeparator());
    }
}
