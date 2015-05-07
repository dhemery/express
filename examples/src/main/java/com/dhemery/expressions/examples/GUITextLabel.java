package com.dhemery.expressions.examples;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

public class GUITextLabel implements SelfDescribing {
    private final String text;
    private final boolean isVisible;

    public GUITextLabel(String text, boolean isVisible) {
        this.text = text;
        this.isVisible = isVisible;
    }

    public String text() {
        return text;
    }

    public boolean isVisible() { return isVisible; }

    @Override
    public void describeTo(Description description) {
        description.appendText(getClass().getSimpleName()).appendText("(").appendValue(text).appendText(")");
    }
}
