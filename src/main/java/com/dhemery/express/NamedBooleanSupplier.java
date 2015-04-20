package com.dhemery.express;

import java.util.function.BooleanSupplier;

/**
 * A boolean supplier with a fixed name.
 * The {@code toString()} method
 * returns the fixed name.
 */
public class NamedBooleanSupplier extends Named implements BooleanSupplier {
    private final BooleanSupplier supplier;

    /**
     * Create a named boolean supplier.
     * @param name the name of this supplier
     * @param supplier the underlying supplier
     */
    public NamedBooleanSupplier(String name, BooleanSupplier supplier) {
        super(name);
        this.supplier = supplier;
    }

    /**
     * {@inheritDoc}
     * @return the value returned by the underlying supplier
     */
    @Override
    public boolean getAsBoolean() {
        return supplier.getAsBoolean();
    }
}
