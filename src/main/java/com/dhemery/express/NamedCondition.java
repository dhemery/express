package com.dhemery.express;

import java.util.function.BooleanSupplier;

/**
 * A {@link Condition} with a fixed name.
 * The {@code toString()} method
 * returns the fixed name.
 */
public class NamedCondition extends Named implements Condition {
    private final BooleanSupplier supplier;

    /**
     * Create a {@link Condition}
     * with the given name
     * and underlying supplier.
     */
    public NamedCondition(String description, BooleanSupplier supplier) {
        super(description);
        this.supplier = supplier;
    }

    /**
     * {@inheritDoc}
     * This implementation delegates to the underlying supplier.
     */
    @Override
    public boolean getAsBoolean() {
        return supplier.getAsBoolean();
    }

    /**
     * {@inheritDoc}
     * This implementation yields its name as its expectation.
     */
    @Override
    public String expectation() { return toString(); }
}
