package org.jboss.arquillian.qunit.junit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.qunit.common.ReflectOperations;
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

    public Method[] getMethods() {
        return suiteClass.getMethods();
    }

    public ArrayList<QUnitTestMethod> getSortedQunitMethods() {
        if (!ArrayUtils.isEmpty(suiteClass.getMethods())) {
            ArrayList<QUnitTestMethod> qunitMethodsAL = new ArrayList<QUnitTestMethod>();
            for (Method m : suiteClass.getMethods()) {
                final Annotation qunitTestAnnotation = ReflectOperations.getAnnotation(m, QUnitTest.class);
                final Annotation sequenceAnnotation = ReflectOperations.getAnnotation(m, InSequence.class);
                final QUnitTestMethod qm = new QUnitTestMethod(m,
                    (sequenceAnnotation != null ? ((InSequence) sequenceAnnotation).value() : 0), m.getAnnotations());
                if (qunitTestAnnotation != null && !StringUtils.isEmpty(((QUnitTest) qunitTestAnnotation).value())) {
                    qunitMethodsAL.add(qm);
                }
            }
            Collections.sort(qunitMethodsAL, QUnitTestMethodComparator.getInstance());
            return qunitMethodsAL;
        }
        return null;
    }

    public Method getDeploymentMethod() {
        return ReflectOperations.findFirstMethodWithAnnotation(getMethods(), Deployment.class);
    }

    public LinkedHashSet<String> getSortedUniqueQunitTestValues() {

        ArrayList<QUnitTestMethod> sortedQunitMethods = getSortedQunitMethods();

        if (!CollectionUtils.isEmpty(sortedQunitMethods)) {
            final LinkedHashSet<String> qunitTestValuesHS = new LinkedHashSet<String>();
            for (QUnitTestMethod qm : sortedQunitMethods) {
                final String qunitTestValue = ((QUnitTest) ReflectOperations.getAnnotation(qm.getMethod(), QUnitTest.class))
                    .value();
                if (!StringUtils.isEmpty(qunitTestValue)) {
                    qunitTestValuesHS.add(qunitTestValue);
                }
            }
            return qunitTestValuesHS;
        }
        return null;
    }
}
