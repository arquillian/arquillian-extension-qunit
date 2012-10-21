package org.jboss.arquillian.dynamic.context;

public interface DynamicExecutor<T> {

    void execute(DynamicTestClass<T> dynamicTestClass, DynamicTestMethod<T> dynamicTestMethod);
}
