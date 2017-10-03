package com.dhemery.expressions.diagnosing;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NamedPredicateTests {
    @Test
    void delegatesTestToTheUnderlyingPredicate() {
        Predicate<String> startsWithF = new NamedPredicate<>("", s -> s.startsWith("f"));

        assertTrue(startsWithF.test("foo"));
        assertFalse(startsWithF.test("boo"));
    }

    @Test
    void describesItselfWithTheGivenName() {
        String name = "a name of a predicate";
        Predicate<String> predicate = new NamedPredicate<>(name, t -> true);


        assertEquals(name, String.valueOf(predicate));
    }

    @Test
    void or_yieldsAPredicateThatPerformsALogical_OR_OfTheUnderlyingPredicateAndAnother() {
        Predicate<String> startsWithF = new NamedPredicate<>("", s -> s.startsWith("f"));
        Predicate<String> endsWithO = new NamedPredicate<>("", s -> s.endsWith("o"));

        assertTrue(startsWithF.or(endsWithO).test("foo"));
        assertTrue(startsWithF.or(endsWithO).test("fog"));
        assertFalse(startsWithF.or(endsWithO).test("bog"));
        assertTrue(startsWithF.or(endsWithO).test("boo"));
    }

    @Test
    void or_yieldsAPredicateThatDescribesItsComposition() {
        Predicate<String> first = new NamedPredicate<>("first", t -> true);
        Predicate<String> second = t -> true;

        Predicate<String> composed = first.or(second);

        assertEquals(format("(first or %s)", second), String.valueOf(composed));
    }

    @Test
    void and_yieldsAPredicateThatPerformsALogical_AND_OfTheUnderlyingPredicateAndAnother() {
        Predicate<String> startsWithF = new NamedPredicate<>("", s -> s.startsWith("f"));
        Predicate<String> endsWithO = new NamedPredicate<>("", s -> s.endsWith("o"));

        assertTrue(startsWithF.and(endsWithO).test("foo"));
        assertFalse(startsWithF.and(endsWithO).test("fog"));
        assertFalse(startsWithF.and(endsWithO).test("bog"));
        assertFalse(startsWithF.and(endsWithO).test("boo"));
    }

    @Test
    void and_yieldsAPredicateThatDescribesItsComposition() {
        Predicate<String> first = new NamedPredicate<>("first", t -> true);
        Predicate<String> second = t -> true;

        Predicate<String> composed = first.and(second);

        assertEquals(format("(first and %s)", second), String.valueOf(composed));
    }

    @Test
    void negate_yieldsAPredicateThatPerformsALogical_NOT_OfTheUnderlyingPredicate() {
        Predicate<String> startsWithF = new NamedPredicate<>("", s -> s.startsWith("f"));

        assertFalse(startsWithF.negate().test("foo"));
        assertTrue(startsWithF.negate().test("boo"));
    }

    @Test
    void negate_yieldsAPredicateThatDescribesItsComposition() {
        Predicate<String> positive = new NamedPredicate<>("positive", t -> true);

        Predicate<String> composed = positive.negate();

        assertEquals("(not positive)", String.valueOf(composed));
    }
}
