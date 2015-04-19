package com.dhemery.express;

import java.util.StringJoiner;

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

    public static String of(Diagnosable diagnosable) {
        StringJoiner diagnosis = new StringJoiner(System.lineSeparator());
        diagnosis.add(diagnosable.subject().orElse(""));
        diagnosis.add(format(EXPECTATION, diagnosable.expectation()));
        diagnosable.failure().ifPresent(d -> diagnosis.add(format(FAILURE, d)));
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
}
