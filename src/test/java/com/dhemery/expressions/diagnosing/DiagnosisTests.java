package com.dhemery.expressions.diagnosing;


import com.dhemery.expressions.PollingSchedule;
import com.dhemery.expressions.helpers.PollingSchedules;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DiagnosisTests {
    private Function<String, String> function = uncallableFunction();
    private String functionValue = "function value";
    private Predicate<String> predicate = uncallablePredicate();
    private PollingSchedule schedule = PollingSchedules.random();
    private String subject = "subject";
    private BooleanSupplier supplier = uncallableBooleanSupplier();

    @Test
    void booleanSupplier() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                String.format("Expected: %s", supplier)
        );

        String diagnosis = Diagnosis.of(supplier);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    void subjectPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                String.format("Expected: %s", predicate),
                String.format("     but: was %s", subject)
        );

        String diagnosis = Diagnosis.of(subject, predicate);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    void subjectFunctionPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                String.format("Expected: %s %s", function, predicate),
                String.format("     but: %s was %s", function, functionValue)
        );

        String diagnosis = Diagnosis.of(subject, function, predicate, functionValue);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    void polledBooleanSupplier() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                String.format("Expected: %s", supplier),
                String.format("     but: timed out, polling %s", schedule)
        );

        String diagnosis = Diagnosis.of(schedule, supplier);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    void polledSubjectPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                String.format("Expected: %s", predicate),
                String.format("     but: timed out, polling %s", schedule)
        );

        String diagnosis = Diagnosis.of(schedule, subject, predicate);

        assertThat(diagnosis, is(expectedDiagnosis));
    }

    @Test
    void polledSubjectFunctionPredicate() {
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

    private Predicate<String> uncallablePredicate() {
        return Named.predicate("predicate", t -> {
            throw new RuntimeException("Diagnosis unexpectedly evaluated the predicate");
        });
    }
}
