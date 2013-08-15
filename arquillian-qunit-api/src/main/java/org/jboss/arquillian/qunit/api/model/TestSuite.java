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
package org.jboss.arquillian.qunit.api.model;

import java.lang.annotation.Annotation;

import org.junit.runner.Description;

/**
 * An abstraction to describe the test suite.
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public interface TestSuite {

    /**
     * Returns the Suite class
     * 
     * @return Suite Class
     */
    public Class<?> getSuiteClass();

    /**
     * Returns the Type/Class annotations.
     * 
     * @return {@link Annotation}
     */
    public Annotation[] getTypeAnnotations();

    /**
     * Returns the {@link TestMethod} included inside the suite.
     * 
     * @return {@link TestMethod} array
     */
    public TestMethod[] getTestMethods();

    /**
     * Returns the {@link DeploymentMethod} included inside the suite.
     * 
     * @return {@link DeploymentMethod}
     */
    public DeploymentMethod getDeploymentMethod();

    /**
     * Returns the QUnit resources path.
     * 
     * @return QUnit Resources path
     */
    public String getQUnitResources();

    /**
     * Returns the JUnit Decsription.
     * 
     * @return JUnit Description
     */
    public Description getDescription();
}