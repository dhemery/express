package com.dhemery.express;

import java.util.StringJoiner;
import java.util.function.BooleanSupplier;

import static java.lang.String.format;

/**
 * Produces diagnoses
 * suitable for logging
 * and for use as detail messages in {@link Throwable Throwables}.
 */
public class Diagnosis {
    public static final String EXPECTATION = "Expected: %s";
    public static final String FAILURE =     "     but: %s";
    public static final String POLLING =     " polling: %s";

    public static String of(Condition condition) {
        return of((Diagnosable) condition);
    }

    public static String of(Condition condition, PollingSchedule schedule) {
        return of((Diagnosable) condition, schedule);
    }

    public static String of(Diagnosable diagnosable) {
        StringJoiner diagnosis = new StringJoiner(System.lineSeparator());
        diagnosis.add(diagnosable.subject().orElse(""));
        diagnosis.add(format(EXPECTATION, diagnosable.expectation()));
        diagnosable.failure().ifPresent(d -> diagnosis.add(format(FAILURE, d)));
        return diagnosis.toString();
    }

    public static String of(BooleanSupplier supplier, PollingSchedule schedule) {
        if(supplier instanceof Diagnosable) return of((Diagnosable) supplier, schedule);
        StringJoiner diagnosis = new StringJoiner(System.lineSeparator())
                .add("")
                .add(format(EXPECTATION, supplier))
                .add(format(POLLING, schedule));
        return diagnosis.toString();
    }

    public static String of(Diagnosable diagnosable, PollingSchedule schedule) {
        StringJoiner diagnosis = new StringJoiner(System.lineSeparator());
        diagnosis.add(diagnosable.subject().orElse(""));
        diagnosis.add(format(EXPECTATION, diagnosable.expectation()));
        diagnosis.add(format(POLLING, schedule));
        diagnosable.failure().ifPresent(d -> diagnosis.add(format(FAILURE, d)));
        return diagnosis.toString();
    }

    public static String of(BooleanSupplier supplier) {
        if(supplier instanceof Diagnosable) return of((Diagnosable) supplier);
        StringJoiner diagnosis = new StringJoiner(System.lineSeparator());
        diagnosis.add("");
        diagnosis.add(format(EXPECTATION, supplier));
        return diagnosis.toString();
    }
}
