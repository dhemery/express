package com.dhemery.expressions.examples.helpers;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class PrintErrorMessages implements MethodRule {
    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } catch (Throwable throwable) {
                    System.out.format("%nThrown during %s.%s()%n", method.getDeclaringClass().getSimpleName(), method.getName());
                    System.out.format("%s: %s%n", throwable.getClass().getName(), throwable.getMessage());
                }
            }
        };
    }
}
