package com.dhemery.expressions.examples.helpers;

import com.dhemery.expressions.Named;
import com.dhemery.expressions.SelfDescribingFunction;
import com.dhemery.expressions.SelfDescribingPredicate;
import com.dhemery.expressions.examples.GUITextLabel;

public class GUITextLabelExpressions {
    public static GUITextLabel visibleLabel(String text) {
        return new GUITextLabel(text, true);
    }

    public static GUITextLabel invisibleLabel(String text) {
        return new GUITextLabel(text, false);
    }

    public static SelfDescribingFunction<? super GUITextLabel, String> text() {
        return Named.function("text", GUITextLabel::text);
    }

    public static SelfDescribingPredicate<? super GUITextLabel> isVisible() {
        return Named.predicate("is visible", GUITextLabel::isVisible);
    }
}
