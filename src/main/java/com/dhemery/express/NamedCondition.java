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
