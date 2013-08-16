/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
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
import org.jboss.arquillian.qunit.api.exceptions.ArquillianQunitException;
import org.jboss.arquillian.qunit.api.model.DeploymentMethod;
import org.jboss.arquillian.qunit.api.model.TestMethod;
import org.jboss.arquillian.qunit.api.model.TestSuite;
import org.jboss.arquillian.qunit.junit.annotations.QUnitResources;
import org.jboss.arquillian.qunit.junit.utils.ReflectOperations;
import org.jboss.arquillian.qunit.junit.utils.TestMethodComparator;
import org.junit.runner.Description;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public class TestSuiteImpl implements TestSuite {

    private Annotation[] annotations;

    private TestMethod[] testMethod;

    private DeploymentMethod deploymentMethod;

    private Class<?> suiteClass;

    private String qunitReqourcesPath;

    private Description description;

    public TestSuiteImpl(Class<?> suite) throws ArquillianQunitException {
        this.suiteClass = suite;
        this.validateSuite();
    }

    @Override
    public Class<?> getSuiteClass() {
        return this.suiteClass;
    }

    @Override
    public Annotation[] getTypeAnnotations() {
        return this.annotations;
    }

    @Override
    public TestMethod[] getTestMethods() {
        return this.testMethod;
    }

    @Override
    public DeploymentMethod getDeploymentMethod() {
        return this.deploymentMethod;
    }

    @Override
    public String getQUnitResourcesPath() {
        return this.qunitReqourcesPath;
    }

    private void addTestMethod(TestMethod m, int index) {
        this.testMethod[index] = m;
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

    @Override
    public Description getDescription() {
        return this.description;
    }

    public TestSuite build() {
        this.description = Description.createSuiteDescription(this.suiteClass);
        final Method[] methods = this.suiteClass.getMethods();
        if (!ArrayUtils.isEmpty(methods)) {
            this.testMethod = new TestMethod[methods.length];
            int index = 0;
            for (Method method : methods) {
                this.addTestMethod(new TestMethodImpl(method), index++);
            }
            Arrays.sort(this.getTestMethods(), TestMethodComparator.getInstance());
        }
        this.annotations = this.suiteClass.getAnnotations();
        this.qunitReqourcesPath = this.suiteClass.getAnnotation(QUnitResources.class).value();
        final Method deployMethod = ReflectOperations.findFirstMethodWithAnnotation(getSuiteClass().getMethods(),
                Deployment.class);
        this.deploymentMethod = (deployMethod != null) ? new DeploymentMethodImpl(deployMethod) : null;
        return this;
    }

}
