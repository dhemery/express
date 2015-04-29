package com.dhemery.express;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

import java.util.function.Function;
import java.util.function.Predicate;

public class Diagnosis {
    public static String of(SelfDescribing condition) {
        Description description = new StringDescription();
        description
                .appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(condition);
        return description.toString();
    }

    public static String of(Object subject, SelfDescribing predicate) {
        Description description = new StringDescription();
        description
                .appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(predicate).appendText(System.lineSeparator())
                .appendText("     but: was ").appendValue(subject);
        return description.toString();
    }

    public static <T>  String of(T subject, Matcher<? super T> matcher) {
        Description description = new StringDescription();
        description
                .appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(matcher).appendText(System.lineSeparator())
                .appendText("     but: ");
        matcher.describeMismatch(subject, description);
        return description.toString();
    }

    public static <R> String of(Object subject, R value, SelfDescribing function, Matcher<? super R> matcher) {
        Description description = new StringDescription();
        description
                .appendText(String.valueOf(subject)).appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(function).appendText(" ").appendDescriptionOf(matcher).appendText(System.lineSeparator())
                .appendText("     but: ");
        matcher.describeMismatch(value, description);
        return description.toString();
    }

    public static <R> String of(Object subject, R value, SelfDescribing function, SelfDescribing predicate) {
        Description description = new StringDescription();
        description
                .appendText(String.valueOf(subject)).appendText(System.lineSeparator())
                .appendText("Expected: ").appendDescriptionOf(function).appendText(" ").appendDescriptionOf(predicate).appendText(System.lineSeparator())
                .appendText("     but: was ").appendValue(value);
        return description.toString();
    }
}
