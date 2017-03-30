/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.qunit.junit.model;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.qunit.api.model.QUnitAssertion;
import org.jboss.arquillian.qunit.api.model.QUnitTest;
import org.jboss.arquillian.qunit.junit.utils.QUnitConstants;

/**
 *
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 *
 */
public class QUnitTestImpl implements QUnitTest {

    private String name;

    private String moduleName;

    private String runTime;

    private boolean isFailed;

    private int passedAssertions;

    private int failedAssertions;

    private QUnitAssertion[] assertions;

    public boolean isFailed() {
        return isFailed;
    }

    public QUnitTest setFailed(boolean isFailed) {
        this.isFailed = isFailed;
        return this;
    }

    public QUnitAssertion[] getAssertions() {
        return assertions;
    }

    public QUnitTest setAssertions(QUnitAssertion[] assertions) {
        if (assertions != null && assertions.length > 0) {
            this.assertions = Arrays.copyOf(assertions, assertions.length);
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public QUnitTest setName(String name) {
        this.name = name;
        return this;
    }

    public String getModuleName() {
        return moduleName;
    }

    public QUnitTest setModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public String getRunTime() {
        return runTime;
    }

    public QUnitTest setRunTime(String runTime) {
        this.runTime = runTime;
        return this;
    }

    public int getPassedAssertions() {
        return passedAssertions;
    }

    public QUnitTest setPassedAssertions(int passed) {
        this.passedAssertions = passed;
        return this;
    }

    public int getFailedAssertions() {
        return failedAssertions;
    }

    public QUnitTest setFailedAssertions(int failed) {
        this.failedAssertions = failed;
        return this;
    }

    public String getDescriptionName() {
        return (new StringBuilder()).append(StringUtils.trimToEmpty(this.moduleName)).append(QUnitConstants.DELIMITER)
            .append(StringUtils.trimToEmpty(this.name)).toString().replaceAll("\\s+", " ");
    }

    @Override
    public String toString() {
        return (new StringBuilder()).append(super.toString())
            .append(" [moduleName=")
            .append(this.moduleName)
            .append(", name=")
            .append(this.name)
            .append(", isFailed=")
            .append(this.isFailed)
            .append(", runTime=")
            .append(this.runTime)
            .append(", passed=")
            .append(this.passedAssertions)
            .append(", failed=")
            .append(this.failedAssertions)
            .append(", asssertions=")
            .append(assertions)
            .append("]")
            .toString();
    }
}
