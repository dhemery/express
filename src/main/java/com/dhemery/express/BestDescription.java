package com.dhemery.express;

import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.hamcrest.internal.SelfDescribingValue;

public class BestDescription {
    public static String of(SelfDescribing item) {
        return StringDescription.toString(item);
    }

    public static String of(Object item) {
        return BestDescription.of(selfDescribing(item));
    }

    private static SelfDescribing selfDescribing(Object item) {
        if (item instanceof SelfDescribing) return (SelfDescribing) item;
        return new SelfDescribingValue<>(item);
    }
}
