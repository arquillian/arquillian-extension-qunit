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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.qunit.api.model.QUnitAssertion;
import org.jboss.arquillian.qunit.api.model.QUnitTest;
import org.jboss.arquillian.qunit.api.model.TestMethod;
import org.jboss.arquillian.qunit.api.model.TestSuite;
import org.jboss.arquillian.qunit.junit.utils.FileOperations;
import org.jboss.arquillian.qunit.junit.utils.QUnitConstants;
import org.jboss.arquillian.qunit.pages.QUnitPage;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ExplodedExporter;
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
public class QUnitTestCaseSimple {

    private static final Logger LOGGER = Logger.getLogger(QUnitTestCaseSimple.class.getName());

    @Drone
    private WebDriver driver;

    @Page
    private QUnitPage qunitPage;

    private static Map<String, Integer> notifiedTestCounterHM = new HashMap<String, Integer>();

    private static TestSuite suite;

    private static RunNotifier notifier;

    private static Map<String, List<String>> qunitSuiteNameTestsHM = null;

    @Test
    public void executeQUnitTestSuites() throws IOException {

        final Archive<?> archive = DeploymentPackager.getInstance().createPackage(suite);
        QUnitTestCaseSimple.setQunitSuiteNameTestsHM(SuiteReader.getInstance().readQUnitTests(archive, suite));
        final File tempFolder = FileOperations.createDirectory(QUnitConstants.TMP_FOLDER);
        archive.as(ExplodedExporter.class).exportExploded(tempFolder);

        final TestMethod[] qunitTestMethods = suite.getTestMethods();
        if (!ArrayUtils.isEmpty(qunitTestMethods)) {
            for (TestMethod testMethod : qunitTestMethods) {
                if (!StringUtils.isEmpty(testMethod.getQUnitTestSuiteFilePath())) {

                    executeQunitTestSuite(testMethod, archive);

                    if (qunitSuiteNameTestsHM.get(testMethod.getQUnitTestSuiteFilePath()) != null
                            && !qunitSuiteNameTestsHM.get(testMethod.getQUnitTestSuiteFilePath()).isEmpty()) {
                        for (String notFinishedTest : qunitSuiteNameTestsHM.get(testMethod.getQUnitTestSuiteFilePath())) {

                            final Description desc = Description.createTestDescription(testMethod.getMethod()
                                    .getDeclaringClass(), getTestNameForNotifier(notFinishedTest));

                            notifier.fireTestFailure(new Failure(desc, new Exception(
                                    "QUnit test was not executed or stuck and did not finish within time")));

                            increaseNotifiedTestCounter(notFinishedTest);
                        }
                    }

                }
            }
        }

        try {
            FileOperations.deleteDirectory(QUnitConstants.TMP_FOLDER);
        } catch (IOException ignore) {
            LOGGER.log(Level.WARNING, "deleteDirectory Error", ignore);
        }
    }

    private void executeQunitTestSuite(TestMethod testMethod, Archive<?> archive) {

        try {

            final String qunitTestFilePath = (new StringBuilder()).append(QUnitConstants.TMP_FOLDER).append("/")
                    .append(archive.getName()).append("/").append(testMethod.getQUnitTestSuiteFilePath()).toString();

            URL url = new File(qunitTestFilePath).toURI().toURL();
            driver.get(url.toExternalForm());

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

                    increaseNotifiedTestCounter(descriptionName);
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
        final int testIndex = notifiedTestCounterHM.containsKey(testName) ? (notifiedTestCounterHM.get(testName) + 1) : 1;
        return new StringBuilder().append("module: ").append(testName.replace(QUnitConstants.DELIMITER, " test: "))
                .append(testIndex > 1 ? " " + testIndex : "").toString();
    }

    private void increaseNotifiedTestCounter(String testName) {
        notifiedTestCounterHM.put(testName,
                notifiedTestCounterHM.containsKey(testName) ? (notifiedTestCounterHM.get(testName) + 1) : 1);
    }

    public static TestSuite getSuite() {
        return suite;
    }

    public static void setSuite(TestSuite suite) {
        QUnitTestCaseSimple.suite = suite;
    }

    public static RunNotifier getNotifier() {
        return notifier;
    }

    public static void setNotifier(RunNotifier notifier) {
        QUnitTestCaseSimple.notifier = notifier;
    }

    public static Map<String, List<String>> getQunitSuiteNameTestsHM() {
        return qunitSuiteNameTestsHM;
    }

    public static void setQunitSuiteNameTestsHM(Map<String, List<String>> qunitSuiteNameTestsHM) {
        QUnitTestCaseSimple.qunitSuiteNameTestsHM = qunitSuiteNameTestsHM;
    }

}
