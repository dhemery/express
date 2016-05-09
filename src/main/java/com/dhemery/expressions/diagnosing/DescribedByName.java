package com.dhemery.expressions.diagnosing;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

/**
 * An object that describes itself by name.
 */
public class DescribedByName implements SelfDescribing {
    private final String name;

    /**
     * Creates an object that describes itself with the given name.
     *
     * @param name
     *         the name of the object
     */
    public DescribedByName(String name) {
        this.name = name;
    }

    /**
     * Returns this object's name.
     *
     * @return this object's name
     */
    @Override
    public final String toString() {
        return name;
    }

    /**
     * Appends this object's name to the description.
     *
     * @param description
     *         the description to append to
     */
    @Override
    public final void describeTo(Description description) {
        description.appendText(name);
    }
}
