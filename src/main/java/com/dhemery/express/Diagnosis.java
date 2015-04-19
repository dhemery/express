package com.dhemery.express;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Produces diagnoses suitable for use
 * as detail messages for {@link Throwable Throwables}.
 */
public class Diagnosis {
    public static final String EXPECTATION_PREFIX = "Expected";
    public static final String RESULT_PREFIX = "but";
    public static final String LINE_SEPARATOR = "\n     ";

    public static Expected expected(Object... expectations) {
        return new Expected(expectations);
    }

    public static String of(Diagnosable diagnosable) {
        return "";
    }

    public static String of(Diagnosable diagnosable, PollingSchedule schedule) {
        return "";
    }

    private static class Entry {
        private final StringJoiner description = new StringJoiner(" ");

        public Entry(String prefix, Object... items) {
            description.add(prefix + ":");
            Arrays.stream(items).map(Object::toString).forEach(description::add);
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }

    public static class Expected extends Entry {
        private Expected(Object... expectations) {
            super(EXPECTATION_PREFIX, expectations);
        }

        public String but(Object result) {
            Result resultLine = new Result(result);
            return new StringJoiner(LINE_SEPARATOR)
                    .add(toString())
                    .add(String.valueOf(resultLine))
                    .toString();
        }

        public <T> String but(T subject, Matcher<? super T> matcher) {
            Result resultLine = new Result(subject, matcher);
            return new StringJoiner(LINE_SEPARATOR)
                    .add(toString())
                    .add(String.valueOf(resultLine))
                    .toString();
        }
    }

    public static class Result extends Entry {
        public Result(Object result) {
            super(RESULT_PREFIX, "was", result);
        }

        public <T> Result(T subject, Matcher<? super T> matcher) {
            super(RESULT_PREFIX, mismatchDescription(subject, matcher));
        }

        private static <T> String mismatchDescription(T subject, Matcher<? super T> matcher) {
            Description description = new StringDescription();
            matcher.describeMismatch(subject, description);
            return description.toString();
        }
    }
}
