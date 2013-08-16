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
package org.jboss.arquillian.qunit.junit.test;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.qunit.api.model.QUnitAssertion;
import org.jboss.arquillian.qunit.api.model.QUnitTest;
import org.jboss.arquillian.qunit.api.model.TestMethod;
import org.jboss.arquillian.qunit.api.model.TestSuite;
import org.jboss.arquillian.qunit.junit.utils.QUnitConstants;
import org.jboss.arquillian.qunit.pages.QUnitPage;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.openqa.selenium.WebDriver;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
@RunWith(Arquillian.class)
@RunAsClient
public class QUnitTestCase {

    private static final Logger LOGGER = Logger.getLogger(QUnitTestCase.class.getName());

    @ArquillianResource
    private URL contextPath;

    @Drone
    private WebDriver driver;

    @Page
    private QUnitPage qunitPage;

    private static Map<String, Integer> testOccurencesHM = new HashMap<String, Integer>();

    private static TestSuite suite;

    private static RunNotifier notifier;

    private static Map<String, List<String>> qunitSuiteNameTestsHM = null;

    @Deployment(testable = false)
    public static Archive<?> deployment() throws IOException {
        return DeploymentPackager.getInstance().createPackage(suite);
    }

    @Test
    public void executeTestCases() {
        final TestMethod[] qunitTestMethods = suite.getTestMethods();
        if (!ArrayUtils.isEmpty(qunitTestMethods)) {
            for (TestMethod testMethod : qunitTestMethods) {
                if (!StringUtils.isEmpty(testMethod.getQUnitTestSuiteFilePath())) {

                    executeQunitTestSuite(testMethod);

                    if (qunitSuiteNameTestsHM.get(testMethod.getQUnitTestSuiteFilePath()) != null
                            && !qunitSuiteNameTestsHM.get(testMethod.getQUnitTestSuiteFilePath()).isEmpty()) {
                        for (String notFinishedTest : qunitSuiteNameTestsHM.get(testMethod.getQUnitTestSuiteFilePath())) {

                            final Description desc = Description.createTestDescription(testMethod.getMethod()
                                    .getDeclaringClass(), getTestNameForNotifier(notFinishedTest));

                            notifier.fireTestFailure(new Failure(desc, new Exception(
                                    "QUnit test was not executed or stuck and did not finish within time")));

                            addNotifiedTest(notFinishedTest);
                        }
                    }

                }
            }
        }
    }

    private void executeQunitTestSuite(TestMethod testMethod) {

        try {

            driver.get((new StringBuilder()).append(contextPath.toExternalForm())
                    .append(testMethod.getQUnitTestSuiteFilePath()).toString());

            qunitPage.waitUntilTestsExecutionIsCompleted();

            final QUnitTest[] qunitTests = qunitPage.getTests();
            if (!ArrayUtils.isEmpty(qunitTests)) {
                final Description suiteDescription = Description.createSuiteDescription(testMethod.getMethod()
                        .getDeclaringClass().getName(), testMethod.getMethod().getAnnotations());
                for (QUnitTest qunitTestResult : qunitTests) {

                    final String descriptionName = qunitTestResult.getDescriptionName();

                    if (qunitSuiteNameTestsHM != null
                            && qunitSuiteNameTestsHM.containsKey(testMethod.getQUnitTestSuiteFilePath())
                            && qunitSuiteNameTestsHM.get(testMethod.getQUnitTestSuiteFilePath()) != null
                            && qunitSuiteNameTestsHM.get(testMethod.getQUnitTestSuiteFilePath()).contains(descriptionName)) {

                        qunitSuiteNameTestsHM.get(testMethod.getQUnitTestSuiteFilePath()).remove(
                                qunitSuiteNameTestsHM.get(testMethod.getQUnitTestSuiteFilePath()).indexOf(descriptionName));

                    }

                    final Description notifierDescription = Description.createTestDescription(testMethod.getMethod()
                            .getDeclaringClass(), getTestNameForNotifier(descriptionName));

                    suiteDescription.addChild(notifierDescription);
                    notifier.fireTestStarted(notifierDescription);
                    if (qunitTestResult.isFailed()) {
                        notifier.fireTestFailure(new Failure(notifierDescription, new Exception(
                                generateFailedMessage(qunitTestResult.getAssertions()))));
                    } else {
                        notifier.fireTestFinished(notifierDescription);
                    }

                    addNotifiedTest(descriptionName);
                }

                suite.getDescription().addChild(suiteDescription);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error: executeQunitTestSuite: ", ex);
        }
    }

    private String generateFailedMessage(QUnitAssertion[] assertions) {
        return (new StringBuilder()).append("Failed ").append(qunitPage.getFailedAssertionMessages(assertions)).toString();
    }

    private String getTestNameForNotifier(String testName) {
        final int testIndex = getIndexForTest(testName);

        return (testIndex > 1) ? new StringBuilder().append("module_name: ")
                .append(testName.replace(QUnitConstants.DELIMITER, " test_name: ")).append(" (").append(testIndex).append(")")
                .toString() : new StringBuilder().append("module_name: ")
                .append(testName.replace(QUnitConstants.DELIMITER, " test_name: ")).toString();
    }

    private void addNotifiedTest(String testName) {
        testOccurencesHM.put(testName, testOccurencesHM.containsKey(testName) ? (testOccurencesHM.get(testName) + 1) : 1);
    }

    private int getIndexForTest(String testName) {
        return testOccurencesHM.containsKey(testName) ? (testOccurencesHM.get(testName) + 1) : 1;
    }

    public static TestSuite getSuite() {
        return suite;
    }

    public static void setSuite(TestSuite suite) {
        QUnitTestCase.suite = suite;
    }

    public static RunNotifier getNotifier() {
        return notifier;
    }

    public static void setNotifier(RunNotifier notifier) {
        QUnitTestCase.notifier = notifier;
    }

    public static Map<String, List<String>> getQunitSuiteNameTestsHM() {
        return qunitSuiteNameTestsHM;
    }

    public static void setQunitSuiteNameTestsHM(Map<String, List<String>> qunitSuiteNameTestsHM) {
        QUnitTestCase.qunitSuiteNameTestsHM = qunitSuiteNameTestsHM;
    }

    public static Map<String, Integer> getTestOccurencesHM() {
        return testOccurencesHM;
    }

    public static void setTestOccurencesHM(Map<String, Integer> testOccurencesHM) {
        QUnitTestCase.testOccurencesHM = testOccurencesHM;
    }

}
