package com.dhemery.expressions;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

/**
 * Diagnoses failed conditions, incorporating all of the given information.
 */
public class Diagnosis {
    private static final SelfDescribing NO_SUBJECT = ignoredDescription -> {
    };

    public static String of(SelfDescribingBooleanSupplier supplier) {
        return diagnosis(
                NO_SUBJECT,
                expected(supplier)
        );
    }

    public static <T> String of(T subject, SelfDescribingPredicate<? super T> predicate) {
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

    public static <T, V> String of(T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher, V functionValue) {
        return diagnosis(
                subject,
                expected(function, matcher),
                but(function, matcherRejected(matcher, functionValue))
        );
    }

    public static <T, V> String of(T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate, V functionValue) {
        return diagnosis(
                subject,
                expected(function, predicate),
                but(function, was(functionValue))
        );
    }

    public static String of(PollingSchedule schedule, SelfDescribingBooleanSupplier supplier) {
        return diagnosis(
                NO_SUBJECT,
                expected(supplier),
                but(timedOutPolling(schedule))
        );
    }

    public static <T> String of(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        return diagnosis(
                subject,
                expected(predicate),
                but(timedOutPolling(schedule))
        );
    }

    public static <T, V> String of(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate, V finalFunctionValue) {
        return diagnosis(
                subject,
                expected(function, predicate),
                but(timedOutPolling(schedule)),
                onFinalEvaluation(function, was(finalFunctionValue))
        );
    }

    public static <T, V> String of(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher, V finalFunctionValue) {
        return diagnosis(
                subject,
                expected(function, matcher),
                but(timedOutPolling(schedule)),
                onFinalEvaluation(function, matcherRejected(matcher, finalFunctionValue))
        );
    }

    private static String diagnosis(Object subject, String... lines) {
        String subjectLine = BestDescription.of(subject) + System.lineSeparator();
        return Arrays.stream(lines).collect(joining(System.lineSeparator(), subjectLine, ""));
    }

    private static String expected(SelfDescribing... details) {
        return line("Expected", Arrays.stream(details).map(StringDescription::toString));
    }

    private static String but(String... details) {
        return line("but", Arrays.stream(details));
    }

    private static String but(SelfDescribing function, String details) {
        return but(StringDescription.toString(function), details);
    }

    private static String onFinalEvaluation(SelfDescribing function, String details) {
        return line("final", Stream.of(StringDescription.toString(function), details));
    }

    private static String line(String label, Stream<String> details) {
        return details.collect(joining(" ", format("%8s: ", label), ""));
    }

    private static String matcherRejected(Matcher<?> matcher, Object actualValue) {
        Description description = new StringDescription();
        matcher.describeMismatch(actualValue, description);
        return String.valueOf(description);
    }

    private static String timedOutPolling(PollingSchedule schedule) {
        return String.join(" ", "timed out, polling", String.valueOf(schedule));
    }

    private static String was(Object item) {
        return format("was %s", BestDescription.of(item));
    }
}
