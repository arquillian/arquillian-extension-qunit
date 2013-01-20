package org.jboss.arquillian.qunit.junit;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.junit.runner.Description;

public class TestModule {

    private String name;
    private Class<?> suiteClass;
    private Description description;
    private LinkedHashMap<String, TestFunction> functions = new LinkedHashMap<String, TestFunction>();

    public TestModule(Class<?> suiteClass, String name) {
        this.suiteClass = suiteClass;
        this.name = name;
        this.description = Description.createSuiteDescription(name);
    }

    public TestFunction addFunction(String name, int testNumber) {
        TestFunction function = new TestFunction(suiteClass, this, name, testNumber);
        functions.put(name, function);
        description.addChild(function.getDescription());
        return function;
    }

    public TestFunction getFunction(String name) {
        return functions.get(name);
    }

    public String getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public Collection<TestFunction> getFunctions() {
        return Collections.unmodifiableCollection(functions.values());
    }

    public boolean isDone() {
        for (TestFunction function : functions.values()) {
            if (!function.isDone()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "TestModule [name=" + name + ", functions=" + functions + "]";
    }
}