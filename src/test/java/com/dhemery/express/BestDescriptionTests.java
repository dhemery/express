package com.dhemery.express;

import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BestDescriptionTests {
    @Test
    public void stringDescriptionOfItem_ifItemTypeSelfDescribing() {
        SelfDescribing item = description -> description.appendText("self description");
        assertThat(BestDescription.of(item), is(StringDescription.toString(item)));
    }

    @Test
    public void stringDescriptionOfItem_ifItemImplementsSelfDescribing() {
        Object item = (SelfDescribing) description -> description.appendText("self description");
        assertThat(BestDescription.of(item), is(StringDescription.toString((SelfDescribing) item)));
    }

    @Test
    public void asFormattedByStringDescriptionAppendValue_ifItemIsNotSelfDescribing() {
        Object item = new Object();
        String requiredDescription = new StringDescription()
                .appendValue(item)
                .toString();

        assertThat(BestDescription.of(item), is(requiredDescription));
    }
}
