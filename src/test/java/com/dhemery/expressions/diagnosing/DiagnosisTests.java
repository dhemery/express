package com.dhemery.expressions.diagnosing;


import com.dhemery.expressions.Named;
import com.dhemery.expressions.SelfDescribingBooleanSupplier;
import com.dhemery.expressions.SelfDescribingFunction;
import com.dhemery.expressions.SelfDescribingPredicate;
import com.dhemery.expressions.helpers.PollingSchedules;
import com.dhemery.expressions.PollingSchedule;
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
    public void booleanSupplier() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                "Expected: " + StringDescription.toString(supplier)
        );

        String diagnosis = Diagnosis.of(supplier);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void subjectMatcher() {
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
    public void subjectPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "", "Expected: " + StringDescription.toString(predicate),
                "     but: was " + new StringDescription().appendValue(subject)
        );

        String diagnosis = Diagnosis.of(subject, predicate);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void subjectFunctionMatcher() {
        StringDescription mismatchDescription = new StringDescription();
        matcher.describeMismatch(functionValue, mismatchDescription);

        String expectedDiagnosis = String.join(System.lineSeparator(),
                BestDescription.of(subject),
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(matcher),
                "     but: " + StringDescription.toString(function) + " " + mismatchDescription
        );

        String diagnosis = Diagnosis.of(subject, function, matcher, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void subjectFunctionPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                BestDescription.of(subject),
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(predicate),
                "     but: " + StringDescription.toString(function) + " was " + new StringDescription().appendValue(functionValue)
        );

        String diagnosis = Diagnosis.of(subject, function, predicate, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void polledBooleanSupplier() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                "Expected: " + StringDescription.toString(supplier),
                "     but: timed out, polling " + schedule
        );

        String diagnosis = Diagnosis.of(schedule, supplier);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void polledSubjectPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                BestDescription.of(subject),
                "Expected: " + StringDescription.toString(predicate),
                "     but: timed out, polling " + schedule
        );

        String diagnosis = Diagnosis.of(schedule, subject, predicate);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void polledSubjectFunctionMatcher() {
        StringDescription mismatchDescription = new StringDescription();
        matcher.describeMismatch(functionValue, mismatchDescription);

        String expectedDiagnosis = String.join(System.lineSeparator(),
                BestDescription.of(subject),
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(matcher),
                "     but: timed out, polling " + schedule,
                "   final: " + StringDescription.toString(function) + " " + mismatchDescription
        );

        String diagnosis = Diagnosis.of(schedule, subject, function, matcher, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void polledSubjectFunctionPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                BestDescription.of(subject),
                "Expected: " + StringDescription.toString(function) + " " + StringDescription.toString(predicate),
                "     but: timed out, polling " + schedule,
                "   final: " + StringDescription.toString(function) + " was " + new StringDescription().appendValue(functionValue)
        );

        String diagnosis = Diagnosis.of(schedule, subject, function, predicate, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    private SelfDescribingBooleanSupplier uncallableBooleanSupplier() {
        return Named.booleanSupplier("supplier", () -> {
            throw new RuntimeException("Diagnosis unexpectedly evaluated the supplier");
        });
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
}
