package com.dhemery.express.descriptive;

import com.dhemery.express.Condition;
import com.dhemery.express.NamedCondition;
import org.junit.Test;

import java.util.Optional;
import java.util.function.BooleanSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

// TODO: Test diagnosis methods
public class NamedConditionTests {
    private static final String ARBITRARY_NAME = null;
    private static final BooleanSupplier ALWAYS_SATISFIED = () -> true;
    private  static final BooleanSupplier NEVER_SATISFIED = () -> false;

    @Test
    public void delegatesIsSatisfiedToTheUnderlyingBooleanSupplier() {
        Condition descriptiveCondition = new NamedCondition(ARBITRARY_NAME, ALWAYS_SATISFIED);
        assertThat(descriptiveCondition.getAsBoolean(), equalTo(ALWAYS_SATISFIED.getAsBoolean()));

        descriptiveCondition = new NamedCondition(ARBITRARY_NAME, NEVER_SATISFIED);
        assertThat(descriptiveCondition.getAsBoolean(), equalTo(NEVER_SATISFIED.getAsBoolean()));
    }

    @Test
    public void describesItselfWithTheGivenName() {
        String name = "It was a dark and stormy night";
        Condition condition = new NamedCondition(name, () -> true);
        assertThat(String.valueOf(condition), is(name));
    }
}
