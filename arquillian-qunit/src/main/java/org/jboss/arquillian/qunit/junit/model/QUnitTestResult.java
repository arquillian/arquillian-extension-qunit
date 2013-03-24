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

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public class QUnitTestResult {

    private String name;

    private String moduleName;

    private String runTime;

    private boolean isFailed;

    private int passed;

    private int failed;

    private int index;

    private QUnitAssertion[] assertions;

    public boolean isFailed() {
        return isFailed;
    }

    public QUnitTestResult setFailed(boolean isFailed) {
        this.isFailed = isFailed;
        return this;
    }

    public QUnitAssertion[] getAssertions() {
        return assertions;
    }

    public int getIndex() {
        return index;
    }

    public QUnitTestResult setIndex(int index) {
        this.index = index;
        return this;
    }

    public QUnitTestResult setAssertions(QUnitAssertion[] assertions) {
        this.assertions = assertions;
        return this;
    }

    public String getName() {
        return name;
    }

    public QUnitTestResult setName(String name) {
        this.name = name;
        return this;
    }

    public String getModuleName() {
        return moduleName;
    }

    public QUnitTestResult setModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public String getRunTime() {
        return runTime;
    }

    public QUnitTestResult setRunTime(String runTime) {
        this.runTime = runTime;
        return this;
    }

    public int getPassed() {
        return passed;
    }

    public QUnitTestResult setPassed(int passed) {
        this.passed = passed;
        return this;
    }

    public int getFailed() {
        return failed;
    }

    public QUnitTestResult setFailed(int failed) {
        this.failed = failed;
        return this;
    }

    public String getDescriptionName() {
        return (new StringBuilder()).append(StringUtils.trimToEmpty(this.moduleName)).append(":")
                .append(StringUtils.trimToEmpty(this.name)).append("(").append(this.index).append(")").toString();
    }

    @Override
    public String toString() {
        return (new StringBuilder()).append(super.toString()).append(" [moduleName=").append(this.moduleName).append(", name=")
                .append(this.name).append(", isFailed=").append(this.isFailed).append(", runTime=").append(this.runTime)
                .append(", passed=").append(this.passed).append(", failed=").append(this.failed).append(", asssertions=")
                .append(assertions).append("]").toString();
    }
}
