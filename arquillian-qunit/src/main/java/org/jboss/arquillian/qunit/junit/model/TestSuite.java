/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.arquillian.qunit.junit.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.qunit.junit.annotations.QUnitResources;
import org.jboss.arquillian.qunit.junit.comparators.QUnitTestMethodComparator;
import org.jboss.arquillian.qunit.junit.exceptions.ArquillianQunitException;
import org.jboss.arquillian.qunit.reflection.ReflectOperations;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public class TestSuite {

    private Annotation[] annotations;

    private TestMethod[] testMethod;

    private DeploymentMethod deploymentMethod;

    private Class<?> suiteClass;

    private String qunitResourcesPath;

    public String getQunitResourcesPath() {
        return qunitResourcesPath;
    }

    public TestSuite(Class<?> suiteClass) throws ArquillianQunitException {
        this.suiteClass = suiteClass;
        validateSuite();
    }

    private void validateSuite() throws ArquillianQunitException {
        if (this.suiteClass == null) {
            throw new ArquillianQunitException("SuiteClass is missing");
        }
        if (this.suiteClass != null) {
            final QUnitResources qunitResources = this.suiteClass.getAnnotation(QUnitResources.class);
            if (qunitResources == null || StringUtils.isEmpty(qunitResources.value())) {
                throw new ArquillianQunitException("QunitResources annotation is missing");
            }
        }
        return;
    }

    public Class<?> getSuiteClass() {
        return suiteClass;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public TestMethod[] getTestMethods() {
        return testMethod;
    }

    public DeploymentMethod getDeploymentMethod() {
        return deploymentMethod;
    }

    private void setTestMethod(TestMethod method, int index) {
        this.testMethod[index] = method;
    }

    public TestSuite build() throws ArquillianQunitException {
        this.annotations = suiteClass.getAnnotations();
        this.qunitResourcesPath = suiteClass.getAnnotation(QUnitResources.class).value();
        final Method[] methods = suiteClass.getMethods();
        if (!ArrayUtils.isEmpty(methods)) {
            this.testMethod = new TestMethod[methods.length];
            int index = 0;
            for (Method method : methods) {
                this.setTestMethod((new TestMethod(method)).build(), index++);
            }
            Arrays.sort(this.getTestMethods(), QUnitTestMethodComparator.getInstance());
        }
        final Method deployMethod = ReflectOperations.findFirstMethodWithAnnotation(getSuiteClass().getMethods(),
                Deployment.class);
        this.deploymentMethod = (deployMethod != null) ? new DeploymentMethod(deployMethod) : null;
        return this;
    }
}
