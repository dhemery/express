package com.dhemery.express.descriptive;

import com.dhemery.express.DescriptiveConsumer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DescriptiveConsumerTests {
    private static final String IGNORED_DESCRIPTION = null;

    @Test
    public void delegatesAcceptToTheUnderlyingConsumer() {
        Set<String> consumed = new HashSet<>();
        Consumer<String> consume = new DescriptiveConsumer<>(IGNORED_DESCRIPTION, consumed::add);
        consume.accept("foo");
        assertThat(consumed, contains("foo"));
    }

    @Test
    public void describesItselfWithTheGivenDescription() {
        String description = "A little ray of sunshine";
        Consumer<String> consumer = new DescriptiveConsumer<>(description, t -> {});
        assertThat(String.valueOf(consumer), is(description));
    }

    @Test
    public void andThen_yieldsAConsumerThatPerformsItsOperationThenAnother() {
        List<String> consumed = new ArrayList<>();
        Consumer<String> consume = new DescriptiveConsumer<>(IGNORED_DESCRIPTION, consumed::add );
        Consumer<String> consumeUpperCase = new DescriptiveConsumer<>(IGNORED_DESCRIPTION, s -> consumed.add(s.toUpperCase()));
        consume.andThen(consumeUpperCase).accept("foo");
        assertThat(consumed, contains("foo", "FOO"));
    }

    @Test
    public void andThen_yieldsAConsumerThatDescribesItsComposition() {
        Consumer<String> first = new DescriptiveConsumer<>("first", t -> {});
        Consumer<String> second = new DescriptiveConsumer<>("second", t -> {});
        assertThat(first.andThen(second).toString(), is("first and then second"));
    }
}
