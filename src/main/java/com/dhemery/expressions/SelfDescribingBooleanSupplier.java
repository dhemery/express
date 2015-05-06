package com.dhemery.expressions;

import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;

/**
 * A {@link BooleanSupplier} that can describe itself.
 */
public interface SelfDescribingBooleanSupplier extends BooleanSupplier, SelfDescribing {
}
