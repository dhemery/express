package com.dhemery.express;

import java.util.function.BooleanSupplier;

/**
 * A {@link BooleanSupplier} with a fixed name.
 * The {@code toString()} method
 * returns the fixed name.
 */
public class NamedBooleanSupplier extends Named implements Diagnosable, BooleanSupplier {
    private final BooleanSupplier supplier;

    /**
     * Create a {@link BooleanSupplier}
     * with the given name
     * and underlying supplier.
     */
    public NamedBooleanSupplier(String description, BooleanSupplier supplier) {
        super(description);
        this.supplier = supplier;
    }

    /**
     * @return the value returned by the underlying supplier
     */
    @Override
    public boolean getAsBoolean() {
        return supplier.getAsBoolean();
    }

    /**
     * @return the condition's name
     */
    @Override
    public String expectation() { return toString(); }
}
