package com.dhemery.expressions.diagnosing;

import com.dhemery.expressions.PollingSchedule;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.Arrays;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

/**
 * Diagnoses failed conditions, incorporating all of the given information.
 */
public class Diagnosis {
    private static final String NO_SUBJECT = "";

    public static String of(BooleanSupplier supplier) {
        return diagnosis(
                NO_SUBJECT,
                expected(supplier)
        );
    }

    public static <T> String of(T subject, Predicate<? super T> predicate) {
        return diagnosis(
                NO_SUBJECT,
                expected(predicate),
                but(was(subject))
        );
    }

    public static <T> String of(T subject, Matcher<? super T> matcher) {
        return diagnosis(
                NO_SUBJECT,
                expected(matcher),
                but(matcherRejected(matcher, subject))
        );
    }

    public static <T, V> String of(T subject, Function<? super T, V> function, Matcher<? super V> matcher, V functionValue) {
        return diagnosis(
                subject.toString(),
                expected(function, matcher),
                but(function.toString(), matcherRejected(matcher, functionValue))
        );
    }

    public static <T, V> String of(T subject, Function<? super T, V> function, Predicate<? super V> predicate, V functionValue) {
        return diagnosis(
                subject.toString(),
                expected(function, predicate),
                but(function.toString(), was(functionValue))
        );
    }

    public static String of(PollingSchedule schedule, BooleanSupplier supplier) {
        return diagnosis(
                NO_SUBJECT,
                expected(supplier),
                but(timedOutPolling(schedule))
        );
    }

    public static <T> String of(PollingSchedule schedule, T subject, Predicate<? super T> predicate) {
        return diagnosis(
                subject.toString(),
                expected(predicate),
                but(timedOutPolling(schedule))
        );
    }

    public static <T, V> String of(PollingSchedule schedule, T subject, Function<? super T, V> function, Predicate<? super V> predicate, V finalFunctionValue) {
        return diagnosis(
                subject.toString(),
                expected(function, predicate),
                but(timedOutPolling(schedule)),
                onFinalEvaluation(function, was(finalFunctionValue))
        );
    }

    public static <T, V> String of(PollingSchedule schedule, T subject, Function<? super T, V> function, Matcher<? super V> matcher, V finalFunctionValue) {
        return diagnosis(
                subject.toString(),
                expected(function, matcher),
                but(timedOutPolling(schedule)),
                onFinalEvaluation(function, matcherRejected(matcher, finalFunctionValue))
        );
    }

    private static String diagnosis(String... lines) {
        return Arrays.stream(lines).collect(joining(System.lineSeparator()));
    }

    private static String expected(Object... details) {
        return line("Expected", Arrays.stream(details).map(Object::toString));
    }

    private static String but(String... details) {
        return line("but", Arrays.stream(details));
    }

    private static <T,R> String onFinalEvaluation(Function<T,R> function, String details) {
        return line("final", Stream.of(function.toString(), details));
    }

    private static String line(String label, Stream<String> details) {
        return details.collect(joining(" ", format("%8s: ", label), ""));
    }

    private static String matcherRejected(Matcher<?> matcher, Object actualValue) {
        Description description = new StringDescription();
        matcher.describeMismatch(actualValue, description);
        return description.toString();
    }

    private static String timedOutPolling(PollingSchedule schedule) {
        return String.join(" ", "timed out, polling", String.valueOf(schedule));
    }

    private static String was(Object item) {
        return format("was %s", item);
    }
}
