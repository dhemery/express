package com.dhemery.expressions.examples;

import com.dhemery.expressions.Named;
import com.dhemery.expressions.SelfDescribingBooleanSupplier;
import com.dhemery.expressions.SelfDescribingPredicate;
import com.dhemery.expressions.examples.helpers.PrintErrorMessages;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

import java.util.function.Function;

import static com.dhemery.expressions.Expressions.assertThat;
import static com.dhemery.expressions.examples.helpers.GUITextLabelExpressions.*;
import static org.hamcrest.Matchers.is;

/**
 * These "test" methods demonstrate the error messages
 * included in the {@link AssertionError}
 * thrown by each style of assertion.
 * <p>
 * Each of these test methods throws an {@code AssertionError},
 * but the {@link #printErrorMessages} rule catches each one,
 * prints its type and message, and discards it.
 */
public class AssertionExamples {
    @Rule public MethodRule printErrorMessages = new PrintErrorMessages();

    @Test
    public void booleanSupplier() {
        SelfDescribingBooleanSupplier theWorldIsFlat = Named.booleanSupplier("the world is flat", () -> false);
        assertThat(theWorldIsFlat);
    }

    @Test
    public void subjectPredicate() {
        assertThat(invisibleLabel("foo"), isVisible());
    }

    @Test
    public void subjectMatcher() {
        assertThat("foo", is("FOO"));
    }

    @Test
    public void subjectFunctionPredicate() {
        assertThat(visibleLabel("foo"), text(), isUpperCase());
    }

    @Test
    public void subjectComposedFunctionNegatedPredicate() {
        assertThat(visibleLabel("foo"), text().andThen(toUpperCase()), isUpperCase().negate());
    }

    private Function<String, String> toUpperCase() {
        return Named.function("upper case", String::toUpperCase);
    }

    @Test
    public void subjectFunctionMatcher() {
        assertThat(visibleLabel("foo"), text(), is("FOO"));
    }

    private SelfDescribingPredicate<String> isUpperCase() {
        return Named.predicate("upper case", s -> s != null && s.equals(s.toUpperCase()));
    }
}
