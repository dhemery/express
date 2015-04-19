package com.dhemery.express;

import java.util.function.BooleanSupplier;

/**
 * Represents a diagnosable condition whose satisfaction may change over time.
 */
public interface Condition extends Diagnosable, BooleanSupplier {}
