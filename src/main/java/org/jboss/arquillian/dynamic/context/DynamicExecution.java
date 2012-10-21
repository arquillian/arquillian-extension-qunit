package org.jboss.arquillian.dynamic.context;

import java.util.List;

public class DynamicExecution<T> {

    private List<DynamicTestClass<T>> classes;
    private DynamicExecutor<T> executor;

    public List<DynamicTestClass<T>> getClasses() {
        return classes;
    }

    public DynamicExecutor<T> getExecutor() {
        return executor;
    }
}
