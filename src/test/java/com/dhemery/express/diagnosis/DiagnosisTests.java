package com.dhemery.express.diagnosis;

import com.dhemery.express.Diagnosis;
import com.dhemery.express.Diagnosis.Expected;
import com.dhemery.express.Diagnosis.Result;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DiagnosisTests {
    @Test
    public void expected_prependsExpectationsWithALabel() {
        Expected expected = Diagnosis.expected("expectations");
        assertThat(expected.toString(), is("Expected: expectations"));
    }

    @Test
    public void expected_separatesExpectationsWithSpaces() {
        Expected expected = Diagnosis.expected("foo", "bar", "baz");
        assertThat(expected.toString(), is("Expected: foo bar baz"));
    }

    @Test
    public void result_prependsResultObjectWithALabel() {
        Result result = new Result("result");
        assertThat(result.toString(), is("but: was result"));
    }

    @Test
    public void result_prependsMatcherDiagnosisWithALabel() {
        String subject = "foo";
        Matcher<String> matcher = isEmptyString();
        Result result = new Result(subject, matcher);

        Description diagnosisFromMatcher = new StringDescription();
        matcher.describeMismatch(subject, diagnosisFromMatcher);

        assertThat(result.toString(), is("but: " + diagnosisFromMatcher));
    }

    @Test
    public void but_alignsResultObjectUnderExpected() {
        String diagnosis = Diagnosis.expected("foo").but("bar");
        assertThat(diagnosis, is("Expected: foo\n     but: was bar"));
    }

    @Test
    public void but_alignsMatcherDiagnosisUnderExpected() {
        String subject = "foo";
        Matcher<String> matcher = isEmptyString();
        String diagnosis = Diagnosis.expected(subject, matcher).but(subject, matcher);

        Description expectedDiagnosis = new StringDescription();
        expectedDiagnosis.appendText("Expected: ")
                .appendText(String.valueOf(subject))
                .appendText(" ")
                .appendDescriptionOf(matcher)
                .appendText("\n     but: ");
        matcher.describeMismatch(subject, expectedDiagnosis);

        assertThat(diagnosis, is(expectedDiagnosis.toString()));
    }
}
