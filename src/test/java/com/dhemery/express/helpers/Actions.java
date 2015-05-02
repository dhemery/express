package com.dhemery.express.helpers;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class Actions {
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
