package com.dhemery.express;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An object with a fixed name.
 */
public class Named implements SelfDescribing {
    private final String name;

    /**
     * Creates an object with the given name.
     */
    public Named(String name) {
        this.name = name;
    }

    /**
     * Returns this object's name
     */
    @Override
    public final String toString() {
        return name;
    }

    /**
     * Creates a {@link Function} with the given name and underlying function.
     *
     * @see NamedFunction
     */
    public static <T, R> SelfDescribingFunction<T, R> function(String description, Function<T, R> function) {
        return new NamedFunction<>(description, function);
    }

    /**
     * Creates a {@link Predicate} with the given name and underlying predicate.
     *
     * @see NamedPredicate
     */
    public static <T> SelfDescribingPredicate<T> predicate(String description, Predicate<T> predicate) {
        return new NamedPredicate<>(description, predicate);
    }

    public static SelfDescribingBooleanSupplier booleanSupplier(String name, BooleanSupplier supplier) {
        return new NamedBooleanSupplier(name, supplier);
    }

    @Override
    public final void describeTo(Description description) {
        description.appendText(name);
    }
}