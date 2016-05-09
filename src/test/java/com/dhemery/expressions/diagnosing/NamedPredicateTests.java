package com.dhemery.expressions.diagnosing;

import org.junit.Test;

import java.util.function.Predicate;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NamedPredicateTests {
    @Test
    public void delegatesTestToTheUnderlyingPredicate() {
        Predicate<String> startsWithF = new NamedPredicate<>("", s -> s.startsWith("f"));

        assertThat(startsWithF.test("foo"), is(true));
        assertThat(startsWithF.test("boo"), is(false));
    }

    @Test
    public void describesItselfWithTheGivenName() {
        String name = "a name of a predicate";
        Predicate<String> predicate = new NamedPredicate<>(name, t -> true);

        assertThat(String.valueOf(predicate), is(name));
    }

    @Test
    public void or_yieldsAPredicateThatPerformsALogical_OR_OfTheUnderlyingPredicateAndAnother() {
        Predicate<String> startsWithF = new NamedPredicate<>("", s -> s.startsWith("f"));
        Predicate<String> endsWithO = new NamedPredicate<>("", s -> s.endsWith("o"));

        assertThat(startsWithF.or(endsWithO).test("foo"), is(true));
        assertThat(startsWithF.or(endsWithO).test("fog"), is(true));
        assertThat(startsWithF.or(endsWithO).test("bog"), is(false));
        assertThat(startsWithF.or(endsWithO).test("boo"), is(true));
    }

    @Test
    public void or_yieldsAPredicateThatDescribesItsComposition() {
        Predicate<String> first = new NamedPredicate<>("first", t -> true);
        Predicate<String> second = t -> true;

        Predicate<String> composed = first.or(second);

        assertThat(String.valueOf(composed), is(format("(first or %s)", second)));
    }

    @Test
    public void and_yieldsAPredicateThatPerformsALogical_AND_OfTheUnderlyingPredicateAndAnother() {
        Predicate<String> startsWithF = new NamedPredicate<>("", s -> s.startsWith("f"));
        Predicate<String> endsWithO = new NamedPredicate<>("", s -> s.endsWith("o"));

        assertThat(startsWithF.and(endsWithO).test("foo"), is(true));
        assertThat(startsWithF.and(endsWithO).test("fog"), is(false));
        assertThat(startsWithF.and(endsWithO).test("bog"), is(false));
        assertThat(startsWithF.and(endsWithO).test("boo"), is(false));
    }

    @Test
    public void and_yieldsAPredicateThatDescribesItsComposition() {
        Predicate<String> first = new NamedPredicate<>("first", t -> true);
        Predicate<String> second = t -> true;

        Predicate<String> composed = first.and(second);

        assertThat(String.valueOf(composed), is(format("(first and %s)", second)));
    }

    @Test
    public void negate_yieldsAPredicateThatPerformsALogical_NOT_OfTheUnderlyingPredicate() {
        Predicate<String> startsWithF = new NamedPredicate<>("", s -> s.startsWith("f"));

        assertThat(startsWithF.negate().test("foo"), is(false));
        assertThat(startsWithF.negate().test("boo"), is(true));
    }

    @Test
    public void negate_yieldsAPredicateThatDescribesItsComposition() {
        Predicate<String> positive = new NamedPredicate<>("positive", t -> true);

        Predicate<String> composed = positive.negate();

        assertThat(String.valueOf(composed), is("(not positive)"));
    }
}
