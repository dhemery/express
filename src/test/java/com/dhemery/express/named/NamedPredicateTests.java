package com.dhemery.express.named;

import com.dhemery.express.NamedPredicate;
import org.junit.Test;

import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class NamedPredicateTests {
    private static final String IGNORED_DESCRIPTION = null;
    private static final Predicate<String> IGNORED_PREDICATE = t -> false;

    @Test
    public void delegatesTestToTheUnderlyingPredicate() {
        Predicate<String> startsWithF = new NamedPredicate<>(IGNORED_DESCRIPTION, s -> s.startsWith("f"));
        assertThat(startsWithF.test("foo"), is(true));
        assertThat(startsWithF.test("boo"), is(false));
    }

    @Test
    public void describesItselfWithTheGivenName() {
        String name = "a name of a predicate";
        Predicate<String> descriptivePredicate = new NamedPredicate<>(name, IGNORED_PREDICATE);
        assertThat(descriptivePredicate.toString(), is(name));
    }

    @Test
    public void or_yieldsAPredicateThatPerformsALogical_OR_OfThisPredicateAndAnother() {
        Predicate<String> startsWithF = new NamedPredicate<>(IGNORED_DESCRIPTION, s -> s.startsWith("f"));
        Predicate<String> endsWithO = new NamedPredicate<>(IGNORED_DESCRIPTION, s -> s.endsWith("o"));
        assertThat(startsWithF.or(endsWithO).test("foo"), is(true));
        assertThat(startsWithF.or(endsWithO).test("fog"), is(true));
        assertThat(startsWithF.or(endsWithO).test("bog"), is(false));
        assertThat(startsWithF.or(endsWithO).test("boo"), is(true));
    }

    @Test
    public void or_yieldsAPredicateNamedToDescribeItsComposition() {
        Predicate<String> first = new NamedPredicate<>("first", IGNORED_PREDICATE);
        Predicate<String> second = new NamedPredicate<>("second", IGNORED_PREDICATE);
        assertThat(first.or(second).toString(), is("(first or second)"));
    }

    @Test
    public void and_yieldsAPredicateThatPerformsALogical_AND_OfThisPredicateAndAnother() {
        Predicate<String> startsWithF = new NamedPredicate<>(IGNORED_DESCRIPTION, s -> s.startsWith("f"));
        Predicate<String> endsWithO = new NamedPredicate<>(IGNORED_DESCRIPTION, s -> s.endsWith("o"));
        assertThat(startsWithF.and(endsWithO).test("foo"), is(true));
        assertThat(startsWithF.and(endsWithO).test("fog"), is(false));
        assertThat(startsWithF.and(endsWithO).test("bog"), is(false));
        assertThat(startsWithF.and(endsWithO).test("boo"), is(false));
    }

    @Test
    public void and_yieldsAPredicateNamedToDescribeItsComposition() {
        Predicate<String> first = new NamedPredicate<>("first", IGNORED_PREDICATE);
        Predicate<String> second = new NamedPredicate<>("second", IGNORED_PREDICATE);
        assertThat(first.and(second).toString(), is("(first and second)"));
    }

    @Test
    public void negate_yieldsAPredicateThatPerformsALogical_NOT_OfThisPredicate() {
        Predicate<String> startsWithF = new NamedPredicate<>(IGNORED_DESCRIPTION, s -> s.startsWith("f"));
        assertThat(startsWithF.negate().test("foo"), is(false));
        assertThat(startsWithF.negate().test("boo"), is(true));
    }

    @Test
    public void negate_yieldsAPredicateNamedToDescribeItsComposition() {
        Predicate<String> startsWithF = new NamedPredicate<>("starts with F", IGNORED_PREDICATE);
        assertThat(startsWithF.negate().toString(), is("(not starts with F)"));
    }
}
