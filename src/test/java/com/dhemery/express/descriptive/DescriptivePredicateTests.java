package com.dhemery.express.descriptive;

import com.dhemery.express.DescriptivePredicate;
import org.junit.Test;

import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DescriptivePredicateTests {
    private static final String IGNORED_DESCRIPTION = null;
    private static final Predicate<String> IGNORED_PREDICATE = t -> false;

    @Test
    public void delegatesTestToTheUnderlyingPredicate() {
        Predicate<String> startsWithF = new DescriptivePredicate<>(IGNORED_DESCRIPTION, s -> s.startsWith("f"));
        assertThat(startsWithF.test("foo"), is(true));
        assertThat(startsWithF.test("boo"), is(false));
    }

    @Test
    public void describesItselfWithTheGivenDescription() {
        String description = "a description of a predicate";
        Predicate<String> descriptivePredicate = new DescriptivePredicate<>(description, IGNORED_PREDICATE);
        assertThat(descriptivePredicate.toString(), is(description));
    }

    @Test
    public void or_yieldsAPredicateThatPerformsALogical_OR_OfThisPredicateAndAnother() {
        Predicate<String> startsWithF = new DescriptivePredicate<>(IGNORED_DESCRIPTION, s -> s.startsWith("f"));
        Predicate<String> endsWithO = new DescriptivePredicate<>(IGNORED_DESCRIPTION, s -> s.endsWith("o"));
        Predicate<String> startsWithFOrEndsWithO = startsWithF.or(endsWithO);
        assertThat(startsWithFOrEndsWithO.test("foo"), is(true));
        assertThat(startsWithFOrEndsWithO.test("fog"), is(true));
        assertThat(startsWithFOrEndsWithO.test("bog"), is(false));
        assertThat(startsWithFOrEndsWithO.test("boo"), is(true));
    }

    @Test
    public void or_yieldsAPredicateThatDescribesItsComposition() {
        Predicate<String> first = new DescriptivePredicate<>("first", IGNORED_PREDICATE);
        Predicate<String> second = new DescriptivePredicate<>("second", IGNORED_PREDICATE);
        assertThat(first.or(second).toString(), is("(first or second)"));
    }

    @Test
    public void and_yieldsAPredicateThatPerformsALogical_AND_OfThisPredicateAndAnother() {
        Predicate<String> startsWithF = new DescriptivePredicate<>(IGNORED_DESCRIPTION, s -> s.startsWith("f"));
        Predicate<String> endsWithO = new DescriptivePredicate<>(IGNORED_DESCRIPTION, s -> s.endsWith("o"));
        Predicate<String> startsWithFAandEndsWithO = startsWithF.and(endsWithO);
        assertThat(startsWithFAandEndsWithO.test("foo"), is(true));
        assertThat(startsWithFAandEndsWithO.test("fog"), is(false));
        assertThat(startsWithFAandEndsWithO.test("bog"), is(false));
        assertThat(startsWithFAandEndsWithO.test("boo"), is(false));
    }

    @Test
    public void and_yieldsAPredicateThatDescribesItsComposition() {
        Predicate<String> first = new DescriptivePredicate<>("first", IGNORED_PREDICATE);
        Predicate<String> second = new DescriptivePredicate<>("second", IGNORED_PREDICATE);
        assertThat(first.and(second).toString(), is("(first and second)"));
    }

    @Test
    public void negate_yieldsAPredicateThatPerformsALogical_NOT_OfThisPredicate() {
        Predicate<String> startsWithF = new DescriptivePredicate<>(IGNORED_DESCRIPTION, s -> s.startsWith("f"));
        Predicate<String> doesNotStartWithF = startsWithF.negate();
        assertThat(doesNotStartWithF.test("foo"), is(false));
        assertThat(doesNotStartWithF.test("boo"), is(true));
    }

    @Test
    public void negate_yieldsAPredicateThatDescribesItsComposition() {
        Predicate<String> startsWithF = new DescriptivePredicate<>("starts with F", IGNORED_PREDICATE);
        assertThat(startsWithF.negate().toString(), is("(not starts with F)"));
    }
}
