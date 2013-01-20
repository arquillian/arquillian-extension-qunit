package org.jboss.arquillian.qunit.junit;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.junit.runner.Description;

public class TestFile {

    private String name;
    private Description description;
    private Class<?> suiteClass;
    private LinkedHashMap<String, TestModule> modules = new LinkedHashMap<String, TestModule>();

    public TestFile(Class<?> suiteClass, String name) {
        this.suiteClass = suiteClass;
        this.name = name;
        this.description = Description.createSuiteDescription(name);
    }

    public String getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public TestModule getOrAddModule(final String name) {
        TestModule module = modules.get(name);
        if (module == null) {
            module = new TestModule(suiteClass, name);
            description.addChild(module.getDescription());
            modules.put(name, module);
        }

        return module;
    }

    public Collection<TestModule> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    public boolean isDone() {
        for (TestModule module : modules.values()) {
            if (!module.isDone()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "TestFile [name=" + name + ", modules=" + modules + "]";
    }
}
