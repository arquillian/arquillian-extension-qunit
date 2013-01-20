package org.jboss.arquillian.qunit.junit;

import org.junit.runner.Description;

public class TestFunction {

    private String name;
    private int testNumber;
    private Description description;

    private boolean done = false;

    public TestFunction(Class<?> suiteClass, String name, int testNumber) {
        this.name = name;
        this.testNumber = testNumber;
        this.description = Description.createTestDescription(suiteClass, name);
    }

    public String getName() {
        return name;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public Description getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public void markDone() {
        this.done = true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + testNumber;
        return result;
    }

    @Override
    public String toString() {
        return "TestFunction [name=" + name + ", testNumber=" + testNumber + "]";
    }
}
