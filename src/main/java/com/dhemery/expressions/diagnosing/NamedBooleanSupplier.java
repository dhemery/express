package com.dhemery.expressions.diagnosing;

import com.dhemery.expressions.SelfDescribingBooleanSupplier;

import java.util.function.BooleanSupplier;

/**
 * A {@link BooleanSupplier} that describes itself by name.
 */
public class NamedBooleanSupplier extends DescribedByName implements SelfDescribingBooleanSupplier {
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
