package org.jboss.arquillian.dynamic.context;

import java.util.List;

public class DynamicTestClass<T> {

    private String className;
    private List<DynamicTestMethod<T>> methods;

    public String getClassName() {
        return className;
    }

    public List<DynamicTestMethod<T>> getMethods() {
        return methods;
    }
}
