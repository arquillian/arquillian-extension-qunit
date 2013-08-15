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

import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.qunit.api.model.QUnitTest;

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

    private int passed;

    private int failed;

    private int index;

    private QUnitAssertionImpl[] assertions;

    public boolean isFailed() {
        return isFailed;
    }

    public QUnitTestImpl setFailed(boolean isFailed) {
        this.isFailed = isFailed;
        return this;
    }

    public QUnitAssertionImpl[] getAssertions() {
        return assertions;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public QUnitTestImpl setAssertions(QUnitAssertionImpl[] assertions) {
        this.assertions = assertions;
        return this;
    }

    public String getName() {
        return name;
    }

    public QUnitTestImpl setName(String name) {
        this.name = name;
        return this;
    }

    public String getModuleName() {
        return moduleName;
    }

    public QUnitTestImpl setModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public String getRunTime() {
        return runTime;
    }

    public QUnitTestImpl setRunTime(String runTime) {
        this.runTime = runTime;
        return this;
    }

    public int getPassed() {
        return passed;
    }

    public QUnitTestImpl setPassed(int passed) {
        this.passed = passed;
        return this;
    }

    public int getFailed() {
        return failed;
    }

    public QUnitTestImpl setFailed(int failed) {
        this.failed = failed;
        return this;
    }

    public String getDescriptionName() {
        return (new StringBuilder()).append(StringUtils.trimToEmpty(this.moduleName)).append(":")
                .append(StringUtils.trimToEmpty(this.name)).toString();
    }

    @Override
    public String toString() {
        return (new StringBuilder()).append(super.toString()).append(" [moduleName=").append(this.moduleName).append(", name=")
                .append(this.name).append(", isFailed=").append(this.isFailed).append(", runTime=").append(this.runTime)
                .append(", passed=").append(this.passed).append(", failed=").append(this.failed).append(", asssertions=")
                .append(assertions).append("]").toString();
    }
}
