package org.jboss.arquillian.qunit.junit;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.junit.runner.Description;

public class TestSuite {

    private Class<?> suiteClass;

    private Description description;

    private LinkedHashMap<String, TestFile> files = new LinkedHashMap<String, TestFile>();

    public TestSuite(Class<?> suiteClass) {
        this.suiteClass = suiteClass;
        this.description = Description.createSuiteDescription(suiteClass);
    }

    public TestFile getOrAddFile(final String name) {
        TestFile file = files.get(name);
        if (file == null) {
            file = new TestFile(suiteClass, name);
            description.addChild(file.getDescription());
            files.put(name, file);
        }

        return file;
    }

    public TestFile getFile(final String name) {
        return files.get(name);
    }

    public Description getDescription() {
        return description;
    }

    public Collection<TestFile> getFiles() {
        return Collections.unmodifiableCollection(files.values());
    }

    public Class<?> getJavaClass() {
        return suiteClass;
    }

    public String getWebRoot() {
        return suiteClass.getAnnotation(WebRoot.class).value();
    }

    public String getQUnitTest() {
        return suiteClass.getAnnotation(QUnitTest.class).value();
    }

}
