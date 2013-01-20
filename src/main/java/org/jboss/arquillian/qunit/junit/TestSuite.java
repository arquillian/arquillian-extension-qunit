package org.jboss.arquillian.qunit.junit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;

public class TestSuite {

    private Class<?> suiteClass;

    private Description description;

    private List<TestFile> files = new LinkedList<TestFile>();

    public TestSuite(Class<?> suiteClass) {
        this.suiteClass = suiteClass;
        this.description = Description.createSuiteDescription(suiteClass);
    }

    public TestFile addFile(String name) {
        TestFile file = new TestFile(suiteClass, name);
        files.add(file);
        description.addChild(file.getDescription());
        return file;
    }

    public Description getDescription() {
        return description;
    }

    public List<TestFile> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public Class<?> getJavaClass() {
        return suiteClass;
    }

}
