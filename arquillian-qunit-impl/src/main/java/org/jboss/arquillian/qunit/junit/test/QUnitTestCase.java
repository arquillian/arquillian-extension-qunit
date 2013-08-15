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

import java.net.URL;
import java.util.HashMap;
import java.util.List;

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

    @ArquillianResource
    private URL contextPath;

    @Drone
    WebDriver driver;

    @Page
    QUnitPage qunitPage;

    public static HashMap<String, Integer> testName_OccurencesHM = new HashMap<String, Integer>();

    public static TestSuite suite;

    public static RunNotifier notifier;

    public static HashMap<String, List<String>> qunitFileName_TestsHM = null;

    @Deployment(testable = false)
    public static Archive<?> deployment() {
        return DeploymentPackager.createPackage(suite);
    }

    @Test
    public void executeTestCases() {
        final TestMethod[] qunitTestMethods = suite.getTestMethods();
        if (!ArrayUtils.isEmpty(qunitTestMethods)) {
            for (TestMethod testMethod : qunitTestMethods) {
                if (!StringUtils.isEmpty(testMethod.getQunitTestFile())) {

                    try {
                        executeQunitTestFile(testMethod);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (qunitFileName_TestsHM.get(testMethod.getQunitTestFile()) != null
                            && !qunitFileName_TestsHM.get(testMethod.getQunitTestFile()).isEmpty()) {
                        for (String notFinishedTest : qunitFileName_TestsHM.get(testMethod.getQunitTestFile())) {

                            final Description desc = Description.createTestDescription(testMethod.getMethod()
                                    .getDeclaringClass(), getTestNameForNotifier(notFinishedTest));

                            notifier.fireTestFailure(new Failure(desc, new Exception("QUnitTest was not executed after 2 minutes")));

                            addNotifiedTest(notFinishedTest);
                        }
                    }

                }
            }
        }
    }

    private void executeQunitTestFile(TestMethod testMethod) {
        driver.get((new StringBuilder()).append(contextPath.toExternalForm()).append(testMethod.getQunitTestFile()).toString());

        try {
            qunitPage.waitUntilTestsExecutionIsCompleted();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final QUnitTest[] qunitTests = qunitPage.getTests();
        if (!ArrayUtils.isEmpty(qunitTests)) {
            final Description suiteDescription = Description.createSuiteDescription(testMethod.getMethod().getDeclaringClass()
                    .getName(), testMethod.getMethod().getAnnotations());
            for (QUnitTest qunitTestResult : qunitTests) {

                final String descriptionName = qunitTestResult.getDescriptionName();

                final Description testDescription = Description.createTestDescription(testMethod.getMethod()
                        .getDeclaringClass(), getTestNameForNotifier(descriptionName));

                if (qunitFileName_TestsHM != null && qunitFileName_TestsHM.containsKey(testMethod.getQunitTestFile())) {

                    if (qunitFileName_TestsHM.get(testMethod.getQunitTestFile()) != null
                            && qunitFileName_TestsHM.get(testMethod.getQunitTestFile()).contains(
                                    qunitTestResult.getDescriptionName())) {
                        qunitFileName_TestsHM.get(testMethod.getQunitTestFile()).remove(
                                qunitFileName_TestsHM.get(testMethod.getQunitTestFile()).indexOf(descriptionName));
                    }
                }

                suiteDescription.addChild(testDescription);
                notifier.fireTestStarted(testDescription);
                if (qunitTestResult.isFailed()) {
                    notifier.fireTestFailure(new Failure(testDescription, new Exception(generateFailedMessage(qunitTestResult
                            .getAssertions()))));
                } else {
                    notifier.fireTestFinished(testDescription);
                }

                addNotifiedTest(descriptionName);
            }

            suite.getDescription().addChild(suiteDescription);
        }
    }

    private String generateFailedMessage(QUnitAssertion[] assertions) {
        return (new StringBuilder()).append("Failed ").append(qunitPage.getFailedAssertionMessages(assertions)).toString();
    }

    private String getTestNameForNotifier(String testName) {
        final int testIndex = getIndexForTest(testName);

        return (testIndex > 1) ? new StringBuilder().append(testName).append(" (").append(testIndex).append(")").toString()
                : testName;
    }

    private void addNotifiedTest(String testName) {
        testName_OccurencesHM.put(testName,
                testName_OccurencesHM.containsKey(testName) ? (testName_OccurencesHM.get(testName) + 1) : 1);
    }

    private int getIndexForTest(String testName) {
        return testName_OccurencesHM.containsKey(testName) ? (testName_OccurencesHM.get(testName) + 1) : 1;
    }
}
