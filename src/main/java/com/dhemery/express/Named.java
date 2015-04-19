package com.dhemery.express;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An object with a fixed name.
 * The {@code toString()} method
 * returns the fixed name.
 */
public class Named {
    private final String name;

    /**
     * Create an object with the given name.
     */
    public Named(String name) {
        this.name = name;
    }

    /**
     * Return this object's name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Create a {@link BooleanSupplier}
     * with the given name
     * and underlying supplier.
     */
    public static BooleanSupplier condition(String name, BooleanSupplier condition) {
        return new NamedBooleanSupplier(name, condition);
    }

    /**
     * Create a {@link Function}
     * with the given name
     * and underlying function.
     * @see NamedFunction
     */
    public <T,R> Function<T,R> function(String description, Function<? super T, ? extends R> function) {
        return new NamedFunction<>(description, function);
    }

    /**
     * Create a {@link Predicate}
     * with the given name
     * and underlying predicate.
     * @see NamedPredicate
     */
    public <T> Predicate<T> predicate(String description, Predicate<? super T> predicate) {
        return new NamedPredicate<>(description, predicate);
    }
}