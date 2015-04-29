package com.dhemery.express;

import org.hamcrest.SelfDescribing;

import java.util.function.Function;

public interface SelfDescribingFunction<T, R> extends Function<T, R>, SelfDescribing {}
