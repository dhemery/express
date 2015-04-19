package com.dhemery.express.diagnosis;

import com.dhemery.express.Diagnosable;
import com.dhemery.express.Diagnosis;
import com.dhemery.express.PollingSchedule;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.StringJoiner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DiagnosisTests {
    @Test
    public void expectation() {
        Diagnosable diagnosable = () -> "expectation";
        String formattedDiagnosis = new StringJoiner(System.lineSeparator())
                .add("")
                .add("Expected: expectation")
                .toString();
        assertThat(Diagnosis.of(diagnosable), is(formattedDiagnosis));
    }

    @Test
    public void subjectAndExpectation() {
        Diagnosable diagnosable = new Diagnosable() {
            @Override public Optional<String> subject() { return Optional.of("subject"); }
            @Override public String expectation() { return "expectation"; }
        };
        String formattedDiagnosis = new StringJoiner(System.lineSeparator())
                .add("subject")
                .add("Expected: expectation")
                .toString();
        assertThat(Diagnosis.of(diagnosable), is(formattedDiagnosis));
    }

    @Test
    public void subjectExpectationAndFailure() {
        Diagnosable diagnosable = new Diagnosable() {
            @Override public Optional<String> subject() { return Optional.of("subject"); }
            @Override public String expectation() { return "expectation"; }
            @Override public Optional<String> failure() { return Optional.of("failure"); }
        };
        String formattedDiagnosis = new StringJoiner(System.lineSeparator())
                .add("subject")
                .add("Expected: expectation")
                .add("     but: failure")
                .toString();
        assertThat(Diagnosis.of(diagnosable), is(formattedDiagnosis));
    }

    @Test
    public void expectationAndFailure() {
        Diagnosable diagnosable = new Diagnosable() {
            @Override public String expectation() { return "expectation"; }
            @Override public Optional<String> failure() { return Optional.of("failure"); }
        };
        String formattedDiagnosis = new StringJoiner(System.lineSeparator())
                .add("")
                .add("Expected: expectation")
                .add("     but: failure")
                .toString();
        assertThat(Diagnosis.of(diagnosable), is(formattedDiagnosis));
    }

    @Test
    public void expectationAndPollingSchedule() {
        Diagnosable diagnosable = () -> "expectation";
        PollingSchedule schedule = new PollingSchedule(Duration.of(3, ChronoUnit.SECONDS), Duration.of(9, ChronoUnit.HOURS));
        String formattedDiagnosis = new StringJoiner(System.lineSeparator())
                .add("")
                .add("Expected: expectation")
                .add(" polling: every PT3S for PT9H")
                .toString();
        assertThat(Diagnosis.of(diagnosable, schedule), is(formattedDiagnosis));
    }

    @Test
    public void subjectExpectationAndPollingSchedule() {
        Diagnosable diagnosable = new Diagnosable() {
            @Override public Optional<String> subject() { return Optional.of("subject"); }
            @Override public String expectation() { return "expectation"; }
        };
        PollingSchedule schedule = new PollingSchedule(Duration.of(3, ChronoUnit.NANOS), Duration.of(9, ChronoUnit.HOURS));
        String formattedDiagnosis = new StringJoiner(System.lineSeparator())
                .add("subject")
                .add("Expected: expectation")
                .add(" polling: every PT0.000000003S for PT9H")
                .toString();
        assertThat(Diagnosis.of(diagnosable, schedule), is(formattedDiagnosis));
    }


    @Test
    public void subjectExpectationFailureAndPollingSchedule() {
        Diagnosable diagnosable = new Diagnosable() {
            @Override public Optional<String> subject() { return Optional.of("subject"); }
            @Override public String expectation() { return "expectation"; }
            @Override public Optional<String> failure() { return Optional.of("failure"); }
        };
        PollingSchedule schedule = new PollingSchedule(Duration.of(42, ChronoUnit.MICROS), Duration.of(9, ChronoUnit.MILLIS));
        String formattedDiagnosis = new StringJoiner(System.lineSeparator())
                .add("subject")
                .add("Expected: expectation")
                .add(" polling: every PT0.000042S for PT0.009S")
                .add("     but: failure")
                .toString();
        assertThat(Diagnosis.of(diagnosable, schedule), is(formattedDiagnosis));
    }


    @Test
    public void expectationFailureAndPollingSchedule() {
        Diagnosable diagnosable = new Diagnosable() {
            @Override public String expectation() { return "expectation"; }
            @Override public Optional<String> failure() { return Optional.of("failure"); }
        };
        PollingSchedule schedule = new PollingSchedule(Duration.of(2, ChronoUnit.MINUTES), Duration.of(5, ChronoUnit.HOURS));
        String formattedDiagnosis = new StringJoiner(System.lineSeparator())
                .add("")
                .add("Expected: expectation")
                .add(" polling: every PT2M for PT5H")
                .add("     but: failure")
                .toString();
        assertThat(Diagnosis.of(diagnosable, schedule), is(formattedDiagnosis));
    }
}
