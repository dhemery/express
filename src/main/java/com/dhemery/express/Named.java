package com.dhemery.express;

import org.hamcrest.Matcher;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
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
     * Create a {@link Diagnosable} {@link BooleanSupplier}
     * with the given name
     * and underlying supplier.
     */
    public static BooleanSupplier booleanSupplier(String name, BooleanSupplier condition) {
        return new NamedBooleanSupplier(name, condition);
    }

    /**
     * Create a {@link Diagnosable} {@link BooleanSupplier}
     * that indicates whether the subject satisfies the predicate.
     * @param subject the subject to evaluate
     * @param predicate defines a satisfactory subject
     * @param <T> the type of the subject
     * @return a diagnosable supplier that indicates whether the subject satisfies the predicate
     * @see SubjectSatisfiesPredicate
     */
    public static <T> BooleanSupplier booleanSupplier(T subject, Predicate<? super T> predicate) {
        return new SubjectSatisfiesPredicate<>(subject, predicate);
    }

    /**
     * Create a {@link Diagnosable} {@link BooleanSupplier}
     * that indicates whether
     * the characteristic extracted from the subject by the function
     * satisfies the matcher.
     * @param subject the subject to evaluate
     * @param function extracts the pertinent characteristic from the subject
     * @param matcher defines satisfactory values for the characteristic
     * @param <T> the type of the subject
     * @param <R> the type of the function result
     * @return a diagnosable supplier that indicates whether
     * the characteristic extracted from the subject by the function
     * satisfies the matcher
     * @see FunctionOfSubjectSatisfiesMatcher
     */
    public static <T, R> BooleanSupplier booleanSupplier(T subject, Function<? super T, ? extends R> function, Matcher<? super R> matcher) {
        return new FunctionOfSubjectSatisfiesMatcher<>(subject, function, matcher);
    }

    /**
     * Create a {@link Consumer}
     * with the given name
     * and underlying consumer.
     * @see NamedConsumer
     */
    public static <T> Consumer<T> consumer(String description, Consumer<? super T> consumer) {
        return new NamedConsumer<>(description, consumer);
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