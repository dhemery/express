package com.dhemery.expressions.diagnosing;


import com.dhemery.expressions.PollingSchedule;
import com.dhemery.expressions.helpers.PollingSchedules;
import org.junit.jupiter.api.Test;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals(expectedDiagnosis, Diagnosis.of(supplier));
    }

    @Test
    void subjectPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                String.format("Expected: %s", predicate),
                String.format("     but: was %s", subject)
        );

        assertEquals(expectedDiagnosis, Diagnosis.of(subject, predicate));
    }

    @Test
    void subjectFunctionPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                String.format("Expected: %s %s", function, predicate),
                String.format("     but: %s was %s", function, functionValue)
        );

        assertEquals(expectedDiagnosis, Diagnosis.of(subject, function, predicate, functionValue));
    }

    @Test
    void polledBooleanSupplier() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                "",
                String.format("Expected: %s", supplier),
                String.format("     but: timed out, polling %s", schedule)
        );

        assertEquals(expectedDiagnosis, Diagnosis.of(schedule, supplier));
    }

    @Test
    void polledSubjectPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                String.format("Expected: %s", predicate),
                String.format("     but: timed out, polling %s", schedule)
        );

        assertEquals(expectedDiagnosis, Diagnosis.of(schedule, subject, predicate));
    }

    @Test
    void polledSubjectFunctionPredicate() {
        String expectedDiagnosis = String.join(System.lineSeparator(),
                subject,
                String.format("Expected: %s %s", function, predicate),
                String.format("     but: timed out, polling %s", schedule),
                String.format("   final: %s was %s", function, functionValue)
        );

        assertEquals(expectedDiagnosis, Diagnosis.of(schedule, subject, function, predicate, functionValue));
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
