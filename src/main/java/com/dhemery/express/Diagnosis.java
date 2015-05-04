package com.dhemery.express;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

/**
 * Diagnoses failed conditions, incorporating all of the given information in a
 * regular format.
 */
public class Diagnosis {
    public static String of(SelfDescribingBooleanSupplier supplier) {
        Description description = new StringDescription();
        description
                .appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(supplier);
        return description.toString();
    }

    public static <T> String of(T subject, SelfDescribingPredicate<? super T> predicate) {
        Description description = new StringDescription();
        description
                .appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(predicate).appendText(System.lineSeparator())
                .appendText("     but: was ").appendValue(subject);
        return description.toString();
    }

    public static <T> String of(T subject, Matcher<? super T> matcher) {
        Description description = new StringDescription();
        description
                .appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(matcher).appendText(System.lineSeparator())
                .appendText("     but: ");
        matcher.describeMismatch(subject, description);
        return description.toString();
    }

    public static <T, V> String of(T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher, V functionValue) {
        Description description = new StringDescription();
        description
                .appendText(String.valueOf(subject)).appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(function).appendText(" ").appendDescriptionOf(matcher).appendText(System.lineSeparator())
                .appendText("     but: ").appendDescriptionOf(function).appendText(" ");
        matcher.describeMismatch(functionValue, description);
        return description.toString();
    }

    public static <T, V> String of(T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate, V functionValue) {
        Description description = new StringDescription();
        description
                .appendText(String.valueOf(subject)).appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(function).appendText(" ").appendDescriptionOf(predicate).appendText(System.lineSeparator())
                .appendText("     but: was ").appendValue(functionValue);
        return description.toString();
    }

    public static String of(PollingSchedule schedule, SelfDescribingBooleanSupplier supplier) {
        Description description = new StringDescription();
        description
                .appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(supplier).appendText(System.lineSeparator())
                .appendText(" polling: ").appendText(String.valueOf(schedule)).appendText(System.lineSeparator())
                .appendText("     but: timed out");
        return description.toString();
    }

    public static <T> String of(PollingSchedule schedule, T subject, SelfDescribingPredicate<? super T> predicate) {
        Description description = new StringDescription();
        description
                .appendValue(subject).appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(predicate).appendText(System.lineSeparator())
                .appendText(" polling: ").appendText(String.valueOf(schedule)).appendText(System.lineSeparator())
                .appendText("     but: timed out");
        return description.toString();
    }

    public static <T, V> String of(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, SelfDescribingPredicate<? super V> predicate, V finalFunctionValue) {
        Description description = new StringDescription();
        description
                .appendText(String.valueOf(subject)).appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(function).appendText(" ").appendDescriptionOf(predicate).appendText(System.lineSeparator())
                .appendText(" polling: ").appendText(String.valueOf(schedule)).appendText(System.lineSeparator())
                .appendText("     but: timed out").appendText(System.lineSeparator())
                .appendText("   final: ").appendDescriptionOf(function).appendText(" was ").appendValue(finalFunctionValue);
        return description.toString();
    }

    public static <T, V> String of(PollingSchedule schedule, T subject, SelfDescribingFunction<? super T, V> function, Matcher<? super V> matcher, V finalFunctionValue) {
        Description description = new StringDescription();
        description
                .appendText(String.valueOf(subject)).appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(function).appendText(" ").appendDescriptionOf(matcher).appendText(System.lineSeparator())
                .appendText(" polling: ").appendText(String.valueOf(schedule)).appendText(System.lineSeparator())
                .appendText("     but: timed out").appendText(System.lineSeparator())
                .appendText("   final: ").appendDescriptionOf(function).appendText(" ");
        matcher.describeMismatch(finalFunctionValue, description);
        return description.toString();
    }
}
