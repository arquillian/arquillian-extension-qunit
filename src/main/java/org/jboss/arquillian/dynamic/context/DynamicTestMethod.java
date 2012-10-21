package org.jboss.arquillian.dynamic.context;

import java.lang.annotation.Annotation;

public class DynamicTestMethod<T> {

    private String methodName;
    private T executionContext;
    private Class<?>[] parameters;
    private Annotation[] annotations;

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameters() {
        return parameters;
    }

    public T getMethod() {
        return executionContext;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }
}
