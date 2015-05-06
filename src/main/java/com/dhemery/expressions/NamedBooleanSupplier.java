package com.dhemery.expressions;

import java.util.function.BooleanSupplier;

/**
 * A boolean supplier with a fixed name. The {@code toString()} method returns
 * the fixed name.
 */
public class NamedBooleanSupplier extends Named implements SelfDescribingBooleanSupplier {
    private final BooleanSupplier supplier;

    /**
     * Create a named boolean supplier.
     *
     * @param name
     *         the name of this supplier
     * @param supplier
     *         the underlying supplier
     */
    public NamedBooleanSupplier(String name, BooleanSupplier supplier) {
        super(name);
        this.supplier = supplier;
    }

    /**
     * @return the value returned by the underlying supplier
     */
    @Override
    public boolean getAsBoolean() {
        return supplier.getAsBoolean();
    }
}
