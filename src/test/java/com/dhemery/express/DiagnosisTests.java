package com.dhemery.express;


import com.dhemery.express.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;

public class DiagnosisTests {
    public static final SelfDescribingBooleanSupplier A_BOOLEAN_SUPPLIER = Named.booleanSupplier("a boolean supplier", () -> false);
    private static final PollingSchedule A_POLLING_SCHEDULE = new PollingSchedule(Duration.ofMillis(1000), Duration.ofSeconds(60));

    @Test
    public void withBooleanSupplier_diagnosisDescribes_supplier() {
        SelfDescribingBooleanSupplier supplier =  A_BOOLEAN_SUPPLIER;

        String[] expectedDiagnosis = new String[]{
                "",
                "Expected: " + StringDescription.toString(supplier)
        };

        String[] diagnosis = Diagnosis.of(supplier).split(System.lineSeparator());

        assertThat(diagnosis, is(arrayContaining(expectedDiagnosis)));
    }

    @Test
    public void scheduleWithBooleanSupplier_diagnosisDescribes_supplier_schedule() {
        PollingSchedule schedule = A_POLLING_SCHEDULE;
        SelfDescribingBooleanSupplier supplier = A_BOOLEAN_SUPPLIER;

        String[] expectedLines = new String[]{
                "",
                "Expected: " + StringDescription.toString(supplier),
                " polling: " + schedule,
                "     but: timed out"
        };

        String[] lines = Diagnosis.of(schedule, supplier).split(System.lineSeparator());

        assertThat(lines, is(arrayContaining(expectedLines)));
    }

    @Test
    public void withSubjectPredicate_diagnosisDescribes_predicate_subject() {
        String subject = "subject";
        SelfDescribingPredicate<String> predicate = Named.predicate("an empty string", String::isEmpty);

        String[] expectedDiagnosis = new String[] {
                "",
                "Expected: " + StringDescription.toString(predicate),
                "     but: was " + new StringDescription().appendValue(subject)
        };

        String[] diagnosis = Diagnosis.of(subject, predicate).split(System.lineSeparator());

        assertThat(diagnosis, is(arrayContaining(expectedDiagnosis)));
    }

    @Test
    public void scheduleWithSubjectPredicate_diagnosisDescribes_subject_predicate_schedule() {
        PollingSchedule schedule = A_POLLING_SCHEDULE;
        String subject = "subject";
        SelfDescribingPredicate<String> predicate = Named.predicate("an empty string", String::isEmpty);

        String[] expectedDiagnosis = new String[]{
                new StringDescription().appendValue(subject).toString(),
                "Expected: " + StringDescription.toString(predicate),
                " polling: " + schedule,
                "     but: timed out"
        };

        String[] diagnosis = Diagnosis.of(schedule, subject, predicate).split(System.lineSeparator());

        assertThat(diagnosis, is(arrayContaining(expectedDiagnosis)));
    }

    @Test
    public void withSubjectMatcher_diagnosisDescribes_matcher_mismatchOfSubject() {
        String subject = "subject";
        Matcher<String> matcher = is("foo");

        Description mismatchDescription = new StringDescription();
        matcher.describeMismatch(subject, mismatchDescription);
        String[] expectedDiagnosis = new String[]{
                "",
                "Expected: " + StringDescription.toString(matcher),
                "     but: " + mismatchDescription
        };

        String[] diagnosis = Diagnosis.of(subject, matcher).split(System.lineSeparator());

        assertThat(diagnosis, is(arrayContaining(expectedDiagnosis)));
    }

    @Test
    public void withSubjectFunctionMatcher_diagnosisDescribes_subject_matcher_function_mismatchOfFunctionResult() {
        String subject = "subject";
        SelfDescribingFunction<String, String> function = Named.function("upper case", String::toUpperCase);
        Matcher<String> matcher = is("bar");

        String functionValue = function.apply(subject);
        Description mismatchDescription = new StringDescription();
        matcher.describeMismatch(functionValue, mismatchDescription);

        String[] expectedDiagnosis = new String[]{
                subject,
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(matcher),
                "     but: " + StringDescription.toString(function) + " " + mismatchDescription
        };

        String[] diagnosis = Diagnosis.of(subject, function, matcher, functionValue).split(System.lineSeparator());

        assertThat(diagnosis, is(arrayContaining(expectedDiagnosis)));
    }

    @Test
    public void withSubjectFunctionPredicate_diagnosisDescribes_subject_predicate_function_functionResult() {
        String subject = "subject";
        SelfDescribingFunction<String, String> function = Named.function("upper case", String::toUpperCase);
        SelfDescribingPredicate<String> predicate = Named.predicate("equals \"bar\"", "bar"::equals);

        String functionValue = function.apply(subject);

        String[] expectedDiagnosis = new String[]{
                subject,
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(predicate),
                "     but: was " + new StringDescription().appendValue(functionValue)
        };

        String[] diagnosis = Diagnosis.of(subject, function, predicate, functionValue).split(System.lineSeparator());

        assertThat(diagnosis, is(arrayContaining(expectedDiagnosis)));
    }

    @Test
    public void scheduleWithSubjectFunctionPredicate_diagnosisDescribes_subject_predicate_function_functionResult_schedule() {
        PollingSchedule schedule = A_POLLING_SCHEDULE;
        String subject = "subject";
        SelfDescribingFunction<String, String> function = Named.function("upper case", String::toUpperCase);
        SelfDescribingPredicate<String> predicate = Named.predicate("equals \"bar\"", "bar"::equals);

        String functionValue = function.apply(subject);

        String[] expectedDiagnosis = new String[]{
                subject,
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(predicate),
                " polling: " + schedule,
                "     but: final " + StringDescription.toString(function) + " was " + new StringDescription().appendValue(functionValue)
        };

        String[] diagnosis = Diagnosis.of(schedule, subject, function, predicate, functionValue).split(System.lineSeparator());

        assertThat(diagnosis, is(arrayContaining(expectedDiagnosis)));
    }
}
