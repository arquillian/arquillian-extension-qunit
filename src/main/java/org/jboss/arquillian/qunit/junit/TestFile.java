package org.jboss.arquillian.qunit.junit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;

public class TestFile {

    private String name;
    private Description description;
    private Class<?> suiteClass;
    private List<TestModule> modules = new LinkedList<TestModule>();

    public TestFile(Class<?> suiteClass, String name) {
        this.name = name;
        this.description = Description.createTestDescription(suiteClass, name);
    }

    public String getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public TestModule addModule(String name) {
        TestModule module = new TestModule(suiteClass, name);
        modules.add(module);
        description.addChild(module.getDescription());
        return module;
    }

    public List<TestModule> getModules() {
        return Collections.unmodifiableList(modules);
    }

    @Override
    public String toString() {
        return "TestFile [name=" + name + ", modules=" + modules + "]";
    }
}
