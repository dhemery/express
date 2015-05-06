package com.dhemery.expressions;

import com.dhemery.expressions.diagnosing.NamedBooleanSupplier;
import com.dhemery.expressions.diagnosing.NamedFunction;
import com.dhemery.expressions.diagnosing.NamedPredicate;
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
     * Creates an object that describes itself with the given name.
     *
     * @param name
     *         the name of the object
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
     * Decorates the supplier to describe itself with the given name.
     *
     * @param name
     *         the name of the supplier
     * @param supplier
     *         the underlying supplier
     *
     * @return a {@code SelfDescribingBooleanSupplier} that functions like the
     * given supplier and describes itself with the given name.
     */
    public static SelfDescribingBooleanSupplier booleanSupplier(String name, BooleanSupplier supplier) {
        return new NamedBooleanSupplier(name, supplier);
    }

    /**
     * Decorates the function to describe itself with the given name.
     *
     * @param name
     *         the name of the function
     * @param function
     *         the underlying function
     * @param <T>
     *         the type of the input to the function
     * @param <V>
     *         the type of the result of the function
     *
     * @return a {@code SelfDescribingFunction} that functions like the given
     * function and describes itself with the given name.
     */
    public static <T, V> SelfDescribingFunction<T, V> function(String name, Function<T, V> function) {
        return new NamedFunction<>(name, function);
    }

    /**
     * Decorates the predicate to describe itself with the given name.
     *
     * @param description
     *         the name of the function
     * @param predicate
     *         the underlying predicate
     * @param <T>
     *         the type of the input to the predicate
     *
     * @return a {@code SelfDescribingPredicate} that functions like the given
     * predicate and describes itself with the given name.
     */
    public static <T> SelfDescribingPredicate<T> predicate(String description, Predicate<T> predicate) {
        return new NamedPredicate<>(description, predicate);
    }

    @Override
    public final void describeTo(Description description) {
        description.appendText(name);
    }
}