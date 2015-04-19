package com.dhemery.express.descriptive;

import com.dhemery.express.Condition;
import com.dhemery.express.DescriptiveCondition;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class DescriptiveConditionTests {
    private static final String ARBITRARY_NAME = null;
    private static final Condition ALWAYS_SATISFIED = () -> true;
    private  static final Condition NEVER_SATISFIED = () -> false;

    @Test
    public void delegatesIsSatisfiedToTheUnderlyingCondition() {
        Condition descriptiveCondition = new DescriptiveCondition(ARBITRARY_NAME, ALWAYS_SATISFIED);
        assertThat(descriptiveCondition.isSatisfied(), equalTo(ALWAYS_SATISFIED.isSatisfied()));

        descriptiveCondition = new DescriptiveCondition(ARBITRARY_NAME, NEVER_SATISFIED);
        assertThat(descriptiveCondition.isSatisfied(), equalTo(NEVER_SATISFIED.isSatisfied()));
    }

    @Test
    public void delegatesDiagnosisToUnderlyingCondition() {
        Optional<String> emptyDiagnosis = Optional.empty();
        Condition descriptiveCondition = new DescriptiveCondition(ARBITRARY_NAME, conditionWithDiagnosis(emptyDiagnosis));
        assertThat(descriptiveCondition.diagnosis(), equalTo(emptyDiagnosis));

        Optional<String> helpfulDiagnosis = Optional.of("overheated flux capacitor");
        descriptiveCondition = new DescriptiveCondition(ARBITRARY_NAME, conditionWithDiagnosis(helpfulDiagnosis));
        assertThat(descriptiveCondition.diagnosis(), equalTo(helpfulDiagnosis));
    }

    @Test
    public void describesItselfWithTheGivenDescription() {
        String description = "It was a dark and stormy night";
        Condition condition = new DescriptiveCondition(description, () -> true);
        assertThat(String.valueOf(condition), is(description));
    }

    private static Condition conditionWithDiagnosis(final Optional<String> diagnosis) {
        Condition underlyingCondition;
        underlyingCondition = new Condition() {
            @Override  public boolean isSatisfied() { return false; }
            @Override  public Optional<String> diagnosis() { return diagnosis; }
        };
        return underlyingCondition;
    }
}
