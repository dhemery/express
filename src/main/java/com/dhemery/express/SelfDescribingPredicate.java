package com.dhemery.express;

import org.hamcrest.SelfDescribing;

import java.util.function.Predicate;

public interface SelfDescribingPredicate<T> extends Predicate<T>, SelfDescribing {
}
