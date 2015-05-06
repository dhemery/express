package com.dhemery.expressions;

import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;

public interface SelfDescribingBooleanSupplier extends BooleanSupplier, SelfDescribing {
}
