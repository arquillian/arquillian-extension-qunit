package org.jboss.arquillian.qunit.testng;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class TestModule {

    private String name;
    private Set<TestFunction> functions = new LinkedHashSet<TestFunction>();

    public TestModule(String name) {
        super();
        this.name = name;
    }

    void addFunction(TestFunction function) {
        functions.add(function);
    }

    public String getName() {
        return name;
    }

    public Set<TestFunction> getFunctions() {
        return Collections.unmodifiableSet(functions);
    }

    @Override
    public String toString() {
        return "TestModule [name=" + name + ", functions=" + functions + "]";
    }
}
