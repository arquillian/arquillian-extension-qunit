package org.jboss.arquillian.qunit.junit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;

public class TestModule {

    private String name;
    private Class<?> suiteClass;
    private Description description;
    private List<TestFunction> functions = new LinkedList<TestFunction>();

    public TestModule(Class<?> suiteClass, String name) {
        this.name = name;
        this.description = Description.createTestDescription(suiteClass, name);
    }

    public TestFunction addFunction(String name, int testNumber) {
        TestFunction function = new TestFunction(suiteClass, name, testNumber);
        functions.add(function);
        description.addChild(function.getDescription());
        return function;
    }

    public String getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public List<TestFunction> getFunctions() {
        return Collections.unmodifiableList(functions);
    }

    @Override
    public String toString() {
        return "TestModule [name=" + name + ", functions=" + functions + "]";
    }
}