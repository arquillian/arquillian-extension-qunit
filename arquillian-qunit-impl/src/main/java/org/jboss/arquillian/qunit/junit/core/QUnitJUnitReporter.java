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
package org.jboss.arquillian.qunit.junit.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.qunit.api.model.QUnitAssertion;
import org.jboss.arquillian.qunit.api.model.QUnitTest;
import org.jboss.arquillian.qunit.api.model.TestMethod;
import org.jboss.arquillian.qunit.api.model.TestSuite;
import org.jboss.arquillian.qunit.junit.utils.NamingUtils;
import org.jboss.arquillian.qunit.utils.MapUtilities;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class QUnitJUnitReporter {

    private RunNotifier notifier;

    private QUnitTest[] qunitTests;

    private TestMethod testMethod;

    private TestSuite testSuite;

    private Map<String, List<String>> expectedTestsBySuiteName;

    public QUnitJUnitReporter(TestSuite suite, TestMethod testMethod, RunNotifier notifier, QUnitTest[] qunitTests,
            Map<String, List<String>> expectedTestsBySuiteName) {
        this.setNotifier(notifier);
        this.setQunitTests(qunitTests);
        this.setTestMethod(testMethod);
        this.setTestSuite(suite);
        this.setExpectedTestsBySuiteName(expectedTestsBySuiteName);
    }

    private RunNotifier getNotifier() {
        return notifier;
    }

    private void setNotifier(RunNotifier notifier) {
        this.notifier = notifier;
    }

    private QUnitTest[] getQunitTests() {
        return qunitTests;
    }

    private void setQunitTests(QUnitTest[] qunitTests) {
        if (qunitTests != null && qunitTests.length > 0) {
            this.qunitTests = Arrays.copyOf(qunitTests, qunitTests.length);
        }
    }

    private TestMethod getTestMethod() {
        return testMethod;
    }

    private void setTestMethod(TestMethod testMethod) {
        this.testMethod = testMethod;
    }

    private TestSuite getTestSuite() {
        return testSuite;
    }

    private void setTestSuite(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

    private Map<String, List<String>> getExpectedTestsBySuiteName() {
        return expectedTestsBySuiteName;
    }

    private void setExpectedTestsBySuiteName(Map<String, List<String>> expectedTestsBySuiteName) {
        if (expectedTestsBySuiteName != null) {
            this.expectedTestsBySuiteName = MapUtilities.copy(expectedTestsBySuiteName);
        }
    }

    private String generateFailedAssertionMessage(QUnitAssertion[] assertions) {
        if (!ArrayUtils.isEmpty(assertions)) {
            StringBuilder sources = new StringBuilder();
            sources.append("Failed ");
            for (QUnitAssertion assertion : assertions) {
                if (assertion.isFailed() && !StringUtils.isEmpty(assertion.getMessage())) {
                    sources.append(assertion.getMessage()).append(" ");
                }
            }
            return sources.toString();
        }
        return "Failed assertion/s";
    }

    private void decreaseExpectedTests(String qunitSuiteFilePath, String testDescriptionName) {
        final Map<String, List<String>> expectedTestsMap = this.getExpectedTestsBySuiteName();
        if (expectedTestsMap != null && expectedTestsMap.containsKey(qunitSuiteFilePath)
                && expectedTestsMap.get(qunitSuiteFilePath).contains(testDescriptionName)) {
            final int index = expectedTestsMap.get(qunitSuiteFilePath).indexOf(testDescriptionName);
            expectedTestsMap.get(qunitSuiteFilePath).remove(index);
        }
    }

    public void report() {
        if (!ArrayUtils.isEmpty(this.getQunitTests())) {
            for (QUnitTest qunitTest : this.getQunitTests()) {
                // qunit test description name
                final String qunitTestDescriptionName = qunitTest.getDescriptionName();
                // unique test description for notifier
                final Description testDescription = this.getTestSuite().getTestDescriptions()
                        .get(NamingUtils.createUniqueTestName(qunitTestDescriptionName));
                // notify that test started
                this.getNotifier().fireTestStarted(testDescription);
                if (qunitTest.isFailed()) {
                    // notify test failure
                    this.getNotifier().fireTestFailure(
                            new Failure(testDescription, new Exception(
                                    generateFailedAssertionMessage(qunitTest.getAssertions()))));
                } else {
                    // notify test finished
                    this.getNotifier().fireTestFinished(testDescription);
                }
                // decrease expected tests
                decreaseExpectedTests(this.getTestMethod().getQUnitTestSuiteFilePath(), qunitTestDescriptionName);
            }
        }
        reportRemainingTests();
    }

    private void reportRemainingTests() {
        final Map<String, List<String>> expectedTestsMap = this.getExpectedTestsBySuiteName();
        if (expectedTestsMap != null && expectedTestsMap.containsKey(this.getTestMethod().getQUnitTestSuiteFilePath())) {
            final List<String> expectedTests = expectedTestsMap.get(this.getTestMethod().getQUnitTestSuiteFilePath());
            for (String test : expectedTests) {
                final Description testDescription = this.getTestSuite().getTestDescriptions()
                        .get(NamingUtils.createUniqueTestName(test));
                notifier.fireTestStarted(testDescription);
                notifier.fireTestFailure(new Failure(testDescription, new Exception(
                        "QUnit test was not executed or stuck and did not finish within time")));
            }
        }
    }
}
