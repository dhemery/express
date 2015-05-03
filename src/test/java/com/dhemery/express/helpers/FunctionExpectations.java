package com.dhemery.express.helpers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.hamcrest.Matchers.equalTo;

public class FunctionExpectations {
    public static Expectations getAsBooleanReturns(final BooleanSupplier supplier, boolean returnValue) {
        return new Expectations() {{ //@formatter:off
            allowing(supplier).getAsBoolean();
                will(returnValue(returnValue));
        }}; //@formatter:on
    }

    public static Expectations matchesReturns(Matcher<String> matcher, String input, boolean returnValue) {
        return new Expectations() {{ //@formatter:off
            allowing(same(matcher)).method("matches").with(equalTo(input));
                will(returnValue( returnValue));
        }}; //@formatter:on
    }

    public static Expectations testReturns(Predicate<String> predicate, String input, boolean returnValue) {
        return new Expectations() {{ //@formatter:off
            allowing(predicate).test(input);
                will(returnValue( returnValue));
        }}; //@formatter:on
    }

    public static Expectations applyReturns(Function<String, String> function, String input, String returnValue) {
        return new Expectations() {{ //@formatter:off
            allowing(function).apply(input);
                will(returnValue( returnValue));
        }}; //@formatter:on
    }

    public static Action appendItsStringValue() {
        return new Action() {
            @Override
            public Object invoke(Invocation invocation) throws Throwable {
                Object invoked = invocation.getInvokedObject();
                Description description = (Description) invocation.getParameter(0);
                description.appendText(invoked.toString());
                return null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("describe itself");
            }
        };
    }

    public static Action appendTheMismatchDescriptionOfTheItem() {
        return new Action() {
            @Override
            public Object invoke(Invocation invocation) throws Throwable {
                Object failedItem = invocation.getParameter(0);
                Description description = (Description) invocation.getParameter(1);
                description.appendText("was ").appendValue(failedItem);
                return null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("describe the mismatch of the item");
            }
        };
    }
}
