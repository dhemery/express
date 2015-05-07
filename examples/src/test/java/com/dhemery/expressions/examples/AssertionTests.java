package com.dhemery.expressions.examples;

import com.dhemery.expressions.*;
import org.junit.Test;

import static com.dhemery.expressions.Expressions.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * NOTE: All of the tests in this class are designed to fail, to demonstrate
 * the diagnostic message produced by each assertion style.
 */
public class AssertionTests {
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
    public void subjectFunctionNegatedPredicate() {
        assertThat(visibleLabel("FOO"), text(), isUpperCase().negate());
    }

    @Test
    public void subjectFunctionMatcher() {
        assertThat(visibleLabel("foo"), text(), is("FOO"));
    }

    private SelfDescribingPredicate<String> isUpperCase() {
        return Named.predicate("upper case", s -> s != null && s.equals(s.toUpperCase()));
    }

    private SelfDescribingFunction<? super GUITextLabel, String> text() {
        return Named.function("text", GUITextLabel::text);
    }

    private static GUITextLabel visibleLabel(String text) {
        return new GUITextLabel(text, true);
    }

    private static GUITextLabel invisibleLabel(String text) {
        return new GUITextLabel(text, false);
    }

    private SelfDescribingPredicate<? super GUITextLabel> isVisible() {
        return Named.predicate("is visible", GUITextLabel::isVisible);
    }
}
