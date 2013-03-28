/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.jboss.arquillian.qunit.api.model;

/**
 * An abstraction to describe a QUnit test.
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public interface QUnitTest {

    /**
     * Returns whether the QUnit test is failed.
     * 
     * @return true or false
     */
    public boolean isFailed();

    /**
     * Returns the {@link QUnitAssertion} contained inside a QUnit test.
     * 
     * @return {@link QUnitAssertion} array
     */
    public QUnitAssertion[] getAssertions();

    /**
     * Returns the assertion's index position.
     * 
     * @return Index
     */
    public int getIndex();

    /**
     * Returns the QUnit test's name.
     * 
     * @return Name
     */
    public String getName();

    /**
     * Returns the QUnit test's module name.
     * 
     * @return true or false.
     */
    public String getModuleName();

    /**
     * Returns the QUnit test's run time execution.
     * 
     * @return RunTime
     */
    public String getRunTime();

    /**
     * Returns the number of passed assertions.
     * 
     * @return Passed
     */
    public int getPassed();

    /**
     * Returns the number of failed assertions.
     * 
     * @return Failed
     */
    public int getFailed();

    /**
     * Returns the unique description name of the QUnit test.
     * 
     * @return Description Name
     */
    public String getDescriptionName();
}
