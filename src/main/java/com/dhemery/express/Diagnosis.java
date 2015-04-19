package com.dhemery.express;

import java.util.Optional;
import java.util.StringJoiner;

import static java.lang.String.format;

/**
 * Diagnoses operations in a format suitable for use
 * in detail messages for {@link Throwable Throwables}.
 */
public class Diagnosis {
    private static final String EXPECTATION = "Expected: %s";
    private static final String FAILURE =     "     but: %s";
    private static final String POLLING =     " polling: %s";

    /**
     * Diagnose the operation.
     * @param operation the operation to diagnose
     * @return a formatted diagnosis of the operation
     */
    public static String of(Diagnosable operation) {
        return diagnose(operation.subject(), operation.expectation(), operation.failure(), Optional.empty());
    }

    /**
     * Diagnose the polled operation.
     * and the polling schedule.
     * @param operation the operation to diagnose
     * @param schedule the schedule on which the operation was polled
     * @return a formatted diagnosis of the operation
     */
    public static String of(Diagnosable operation, PollingSchedule schedule) {
        return diagnose(operation.subject(), operation.expectation(), operation.failure(), Optional.of(schedule));
    }

    /**
     * Diagnose the operation.
     * The diagnosis includes neither subject nor failure.
     * The string value of the operation is used as the explanation.
     * @param operation the operation to diagnose
     * @return a formatted diagnosis of the operation
     */
    public static String of(Object operation) {
        return of(diagnosable(operation));
    }

    /**
     * Diagnose the polled operation.
     * The diagnosis includes neither subject nor failure.
     * The string value of the operation is used as the explanation.
     * @param operation the operation to diagnose
     * @param schedule the schedule on which the operation was polled
     * @return a formatted diagnosis of the operation
     */
    public static String of(Object operation, PollingSchedule schedule) {
        return of(diagnosable(operation), schedule);
    }

    private static String diagnose(Optional<String> subject, String expectation, Optional<String> failure, Optional<PollingSchedule> schedule) {
        StringJoiner diagnosis = new StringJoiner(System.lineSeparator());
        diagnosis.add(subject.orElse(""));
        diagnosis.add(format(EXPECTATION, expectation));
        schedule.ifPresent(s -> diagnosis.add(format(POLLING, s)));
        failure.ifPresent(d -> diagnosis.add(format(FAILURE, d)));
        return diagnosis.toString();
    }

    private static Diagnosable diagnosable(Object object) {
        return object instanceof Diagnosable ? (Diagnosable) object : object::toString;
    }
}
