package com.dhemery.express;


import com.dhemery.express.helpers.PollingSchedules;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DiagnosisTests {
    SelfDescribingFunction<String, String> function = uncallableFunction();
    String functionValue = "function value";
    Matcher<String> matcher = uncallableMatcher();
    SelfDescribingPredicate<String> predicate = uncallablePredicate();
    PollingSchedule schedule = PollingSchedules.random();
    String subject = "subject";
    SelfDescribingBooleanSupplier supplier = uncallableBooleanSupplier();

    @Test
    public void withBooleanSupplier_diagnosisDescribes_supplier() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                "Expected: " + StringDescription.toString(supplier)
        );

        String diagnosis = Diagnosis.of(supplier);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void scheduleWithBooleanSupplier_diagnosisDescribes_supplier_schedule() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                "Expected: " + StringDescription.toString(supplier),
                " polling: " + schedule,
                "     but: timed out"
        );

        String diagnosis = Diagnosis.of(schedule, supplier);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void withSubjectPredicate_diagnosisDescribes_predicate_subject() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "", "Expected: " + StringDescription.toString(predicate),
                "     but: was " + new StringDescription().appendValue(subject)
        );

        String diagnosis = Diagnosis.of(subject, predicate);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void scheduleWithSubjectPredicate_diagnosisDescribes_subject_predicate_schedule() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                new StringDescription().appendValue(subject).toString(),
                "Expected: " + StringDescription.toString(predicate),
                " polling: " + schedule,
                "     but: timed out"
        );

        String diagnosis = Diagnosis.of(schedule, subject, predicate);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test

    public void withSubjectMatcher_diagnosisDescribes_matcher_mismatchOfSubject() {
        StringDescription mismatchDescription = new StringDescription();
        matcher.describeMismatch(subject, mismatchDescription);

        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                "Expected: " + StringDescription.toString(matcher),
                "     but: " + mismatchDescription
        );

        String diagnosis = Diagnosis.of(subject, matcher);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void withSubjectFunctionMatcher_diagnosisDescribes_subject_matcher_function_mismatchOfFunctionValue() {
        StringDescription mismatchDescription = new StringDescription();
        matcher.describeMismatch(functionValue, mismatchDescription);

        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(matcher),
                "     but: " + StringDescription.toString(function) + " " + mismatchDescription
        );

        String diagnosis = Diagnosis.of(subject, function, matcher, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void scheduleWithSubjectFunctionMatcher_diagnosisDescribes_subject_matcher_function_mismatchOfFunctionValue_schedule() {
        StringDescription mismatchDescription = new StringDescription();
        matcher.describeMismatch(functionValue, mismatchDescription);

        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(matcher),
                " polling: " + schedule,
                "     but: timed out",
                "   final: " + StringDescription.toString(function) + " " + mismatchDescription
        );

        String diagnosis = Diagnosis.of(schedule, subject, function, matcher, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void withSubjectFunctionPredicate_diagnosisDescribes_subject_predicate_function_functionResult() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(predicate),
                "     but: was " + new StringDescription().appendValue(functionValue)
        );

        String diagnosis = Diagnosis.of(subject, function, predicate, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void scheduleWithSubjectFunctionPredicate_diagnosisDescribes_subject_predicate_function_functionResult_schedule() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(predicate),
                " polling: " + schedule,
                "     but: timed out",
                "   final: " + StringDescription.toString(function) + " was " + new StringDescription().appendValue(functionValue)
        );

        String diagnosis = Diagnosis.of(schedule, subject, function, predicate, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    private SelfDescribingFunction<String, String> uncallableFunction() {
        return Named.function("function", t -> {
            throw new RuntimeException("Diagnosis unexpectedly evaluated the function");
        });
    }

    private Matcher<String> uncallableMatcher() {
        return new BaseMatcher<String>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("matcher");
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                description.appendText("mismatch of ").appendValue(item);
            }

            @Override
            public boolean matches(Object item) {
                throw new RuntimeException("Diagnosis unexpectedly evaluated the matcher");
            }
        };
    }

    private SelfDescribingPredicate<String> uncallablePredicate() {
        return Named.predicate("predicate", t -> {
            throw new RuntimeException("Diagnosis unexpectedly evaluated the predicate");
        });
    }

    private SelfDescribingBooleanSupplier uncallableBooleanSupplier() {
        return Named.booleanSupplier("supplier", () -> {
            throw new RuntimeException("Diagnosis unexpectedly evaluated the supplier");
        });
    }
}
