package com.dhemery.expressions.diagnosing;


import com.dhemery.expressions.PollingSchedule;
import com.dhemery.expressions.helpers.PollingSchedules;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DiagnosisTests {
    private Function<String, String> function = uncallableFunction();
    private String functionValue = "function value";
    private Matcher<String> matcher = uncallableMatcher();
    private Predicate<String> predicate = uncallablePredicate();
    private PollingSchedule schedule = PollingSchedules.random();
    private String subject = "subject";
    private BooleanSupplier supplier = uncallableBooleanSupplier();

    @Test
    public void booleanSupplier() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                String.format("Expected: %s", supplier)
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
                String.format("Expected: %s", matcher),
                String.format("     but: %s", mismatchDescription)
        );

        String diagnosis = Diagnosis.of(subject, matcher);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void subjectPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                String.format("Expected: %s", predicate),
                String.format("     but: was %s", subject)
        );

        String diagnosis = Diagnosis.of(subject, predicate);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void subjectFunctionMatcher() {
        StringDescription mismatchDescription = new StringDescription();
        matcher.describeMismatch(functionValue, mismatchDescription);

        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                String.format("Expected: %s %s", function, matcher),
                String.format("     but: %s %s", function, mismatchDescription)
        );

        String diagnosis = Diagnosis.of(subject, function, matcher, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void subjectFunctionPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                String.format("Expected: %s %s", function, predicate),
                String.format("     but: %s was %s", function, functionValue)
        );

        String diagnosis = Diagnosis.of(subject, function, predicate, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void polledBooleanSupplier() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                String.format("Expected: %s", supplier),
                String.format("     but: timed out, polling %s", schedule)
        );

        String diagnosis = Diagnosis.of(schedule, supplier);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void polledSubjectPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                String.format("Expected: %s", predicate),
                String.format("     but: timed out, polling %s", schedule)
        );

        String diagnosis = Diagnosis.of(schedule, subject, predicate);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void polledSubjectFunctionMatcher() {
        StringDescription mismatchDescription = new StringDescription();
        matcher.describeMismatch(functionValue, mismatchDescription);

        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                String.format("Expected: %s %s", function, StringDescription.toString(matcher)),
                String.format("     but: timed out, polling %s", schedule),
                String.format("   final: %s %s", function, mismatchDescription)
        );

        String diagnosis = Diagnosis.of(schedule, subject, function, matcher, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    public void polledSubjectFunctionPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                String.format("Expected: %s %s", function, predicate),
                String.format("     but: timed out, polling %s", schedule),
                String.format("   final: %s was %s", function, functionValue)
        );

        String diagnosis = Diagnosis.of(schedule, subject, function, predicate, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    private BooleanSupplier uncallableBooleanSupplier() {
        return Named.booleanSupplier("supplier", () -> {
            throw new RuntimeException("Diagnosis unexpectedly evaluated the supplier");
        });
    }

    private Function<String, String> uncallableFunction() {
        return Named.function("function", t -> {
            throw new RuntimeException("Diagnosis unexpectedly evaluated the function");
        });
    }

    private Matcher<String> uncallableMatcher() {
        return new BaseMatcher<>() {
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

    private Predicate<String> uncallablePredicate() {
        return Named.predicate("predicate", t -> {
            throw new RuntimeException("Diagnosis unexpectedly evaluated the predicate");
        });
    }
}
