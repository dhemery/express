package com.dhemery.express;

import java.util.function.Consumer;

import static java.lang.String.format;


/**
 * A {@link Consumer} with a fixed name.
 * The {@code toString()} method
 * returns the fixed name.
 * Each composed consumer
 * created by this consumer
 * is named to describe the composition.
 * @param <T> the type of the input to the consumer
 */
public class NamedConsumer<T> extends Named implements Consumer<T> {
    private final Consumer<? super T> consumer;

    /**
     * Create a {@link Consumer}
     * with the given name
     * and underlying consumer.
     */
    public NamedConsumer(String name, Consumer<? super T> consumer) {
        super(name);
        this.consumer = consumer;
    }

    /**
     * {@inheritDoc}
     * This implementation simply calls the underlying consumer.
     */
    @Override
    public void accept(T t) {
        consumer.accept(t);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The composed consumer is named to describe
     * this consumer,
     * the {@code after} consumer,
     * and their composition.
     * </p>
     */
    @Override
    public Consumer<T> andThen(Consumer<? super T> after) {
        return new NamedConsumer<>(format("%s %s %s", this, "and then", after), Consumer.super.andThen(after));
    }
}
