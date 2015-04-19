package com.dhemery.express.descriptive;

import com.dhemery.express.NamedConsumer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class NamedConsumerTests {
    private static final String IGNORED_DESCRIPTION = null;

    @Test
    public void delegatesAcceptToTheUnderlyingConsumer() {
        Set<String> consumed = new HashSet<>();
        Consumer<String> consume = new NamedConsumer<>(IGNORED_DESCRIPTION, consumed::add);
        consume.accept("foo");
        assertThat(consumed, contains("foo"));
    }

    @Test
    public void describesItselfWithTheGivenName() {
        String name = "A little ray of sunshine";
        Consumer<String> consumer = new NamedConsumer<>(name, t -> {});
        assertThat(String.valueOf(consumer), is(name));
    }

    @Test
    public void andThen_yieldsAConsumerThatPerformsItsOperationThenAnother() {
        List<String> consumed = new ArrayList<>();
        Consumer<String> consume = new NamedConsumer<>(IGNORED_DESCRIPTION, consumed::add );
        Consumer<String> consumeUpperCase = new NamedConsumer<>(IGNORED_DESCRIPTION, s -> consumed.add(s.toUpperCase()));
        consume.andThen(consumeUpperCase).accept("foo");
        assertThat(consumed, contains("foo", "FOO"));
    }

    @Test
    public void andThen_yieldsAConsumerNamedToDescribeItsComposition() {
        Consumer<String> first = new NamedConsumer<>("first", t -> {});
        Consumer<String> second = new NamedConsumer<>("second", t -> {});
        assertThat(first.andThen(second).toString(), is("first and then second"));
    }
}
