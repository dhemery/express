package com.dhemery.expressions;

import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.hamcrest.internal.SelfDescribingValue;

/**
 * Describes an object, preferring the object's description of itself.
 */
public class BestDescription {
    /**
     * Returns the item's description of itself.
     *
     * @param item
     *         the item to describe
     *
     * @return the item's description of itself
     */
    public static String of(SelfDescribing item) {
        return StringDescription.toString(item);
    }

    /**
     * Returns a description of the item. If the item is {@link SelfDescribing},
     * its self-description is returned. Otherwise, the description is as
     * formatted by {@link StringDescription#appendValue}.
     *
     * @param item
     *         the item to describe
     *
     * @return a description of the item
     */
    public static String of(Object item) {
        return BestDescription.of(selfDescribing(item));
    }

    private static SelfDescribing selfDescribing(Object item) {
        if (item instanceof SelfDescribing) return (SelfDescribing) item;
        return new SelfDescribingValue<>(item);
    }
}
