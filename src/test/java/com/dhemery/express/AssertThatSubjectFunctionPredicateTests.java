package com.dhemery.express;

import com.dhemery.express.helpers.ExpressionTestBase;
import com.dhemery.express.helpers.FunctionExpectations;
import com.dhemery.express.helpers.Throwables;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AssertThatSubjectFunctionPredicateTests extends ExpressionTestBase {
    private static final String SUBJECT = "subject";
    private static final String FUNCTION_VALUE = "function value";
    @Mock
    SelfDescribingFunction<String, String> function;

    @Mock
    SelfDescribingPredicate predicate;

    @Before
    public void setup() {
        givenThat(FunctionExpectations.applyReturns(function, SUBJECT, FUNCTION_VALUE));
    }

    @Test
    public void withSubjectFunctionPredicate_returnsWithoutThrowing_ifPredicateAcceptsFunctionOfSubject() {
        givenThat(FunctionExpectations.testReturns(predicate, FUNCTION_VALUE, true));

        Expressions.assertThat(SUBJECT, function, predicate);
    }

    @Test(expected = AssertionError.class)
    public void withSubjectFunctionPredicate_throwsAssertionError_ifPredicateRejectsFunctionOfSubject() {
        givenThat(FunctionExpectations.testReturns(predicate, FUNCTION_VALUE, false));

        Expressions.assertThat(SUBJECT, function, predicate);
    }

    @Test
    public void withSubjectFunctionPredicate_diagnosisDescribes_subject_predicate_function_functionResult() {
        givenThat(FunctionExpectations.testReturns(predicate, FUNCTION_VALUE, false));

        String message = Throwables.messageThrownBy(() -> Expressions.assertThat(SUBJECT, function, predicate));

        assertThat(message, is(Diagnosis.of(SUBJECT, function, predicate, FUNCTION_VALUE)));
    }
}
