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
    boolean isFailed();

    /**
     * Returns the {@link QUnitAssertion} contained inside a QUnit test.
     * 
     * @return {@link QUnitAssertion} array
     */
    QUnitAssertion[] getAssertions();

    /**
     * Returns the QUnit test's name.
     * 
     * @return Name
     */
    String getName();

    /**
     * Returns the QUnit test's module name.
     * 
     * @return true or false.
     */
    String getModuleName();

    /**
     * Returns the QUnit test's run time execution.
     * 
     * @return RunTime
     */
    String getRunTime();

    /**
     * Returns the number of passed assertions.
     * 
     * @return Passed
     */
    int getPassedAssertions();

    /**
     * Returns the number of failed assertions.
     * 
     * @return Failed
     */
    int getFailedAssertions();

    /**
     * Returns the unique description name of the QUnit test.
     * 
     * @return Description Name
     */
    String getDescriptionName();

    QUnitTest setAssertions(QUnitAssertion[] qunitAssertions);

    QUnitTest setName(String qunitTestName);

    QUnitTest setPassedAssertions(int passed);

    QUnitTest setFailedAssertions(int failed);

    QUnitTest setRunTime(String runTime);

    QUnitTest setFailed(boolean failed);
}
