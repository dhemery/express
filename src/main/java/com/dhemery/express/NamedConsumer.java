package com.dhemery.express;

import java.util.function.Consumer;

import static java.lang.String.format;


/**
 * A {@link Consumer} with a fixed description.
 * The {@code toString()} method
 * returns the fixed description.
 * Each composed consumer
 * created by this consumer
 * receives a description of the composition.
 * @param <T> the type of the input to the consumer
 */
public class NamedConsumer<T> extends Named implements Consumer<T> {
    private final Consumer<? super T> consumer;

    /**
     * Create a {@link Consumer}
     * with the given description
     * and underlying consumer.
     */
    public NamedConsumer(String description, Consumer<? super T> consumer) {
        super(description);
        this.consumer = consumer;
    }

    /**
     * {@inheritDoc}
     * This implementation delegates to the underlying consumer.
     */
    @Override
    public void accept(T t) {
        consumer.accept(t);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The composed consumer's description
     * describes this consumer,
     * the {@code after} consumer,
     * and their composition.
     * </p>
     */
    @Override
    public Consumer<T> andThen(Consumer<? super T> after) {
        return new NamedConsumer<>(format("%s %s %s", this, "and then", after), Consumer.super.andThen(after));
    }
}
