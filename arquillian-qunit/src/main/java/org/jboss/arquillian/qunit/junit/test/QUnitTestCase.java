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

package org.jboss.arquillian.qunit.junit.test;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.qunit.junit.model.QUnitAssertion;
import org.jboss.arquillian.qunit.junit.model.QUnitTestResult;
import org.jboss.arquillian.qunit.junit.model.TestMethod;
import org.jboss.arquillian.qunit.junit.model.TestSuite;
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

    private Logger logger = Logger.getLogger(QUnitTestCase.class.getSimpleName());

    @ArquillianResource
    private URL contextPath;

    @Drone
    WebDriver driver;

    @Page
    QUnitPage qunitPage;

    private static TestSuite suite;

    private static RunNotifier notifier;

    @Deployment(testable = true)
    public static Archive<?> deployment() {
        return DeploymentPackager.createPackage(getSuite());
    }

    @Test
    public void executeQunitTestFiles() {
        final TestMethod[] qunitTestMethods = suite.getTestMethods();
        if (!ArrayUtils.isEmpty(qunitTestMethods)) {
            for (TestMethod method : qunitTestMethods) {
                if (!StringUtils.isEmpty(method.getQunitTestFile())) {
                    executeQunitTestFile(method.getQunitTestFile());
                }
            }
        }
    }

    private void executeQunitTestFile(String qunitTestFile) {
        driver.get((new StringBuilder()).append(contextPath.toExternalForm()).append(qunitTestFile).toString());

        qunitPage.waitUntilQUnitTestsExecutionIsCompleted();
        final QUnitTestResult[] qunitTestResults = qunitPage.getQUnitTestResults();

        if (!ArrayUtils.isEmpty(qunitTestResults)) {
            for (QUnitTestResult qunitTestResult : qunitTestResults) {
                logger.log(Level.INFO, (new StringBuilder()).append("## QUnitTestResults: ").append(qunitTestResult.toString())
                        .toString());
                final Description description = Description.createTestDescription(suite.getSuiteClass(),
                        qunitTestResult.getDescriptionName());
                notifier.fireTestStarted(description);
                if (qunitTestResult.isFailed()) {
                    notifier.fireTestFailure(new Failure(description, new Exception(generateFailedMessage(qunitTestResult
                            .getAssertions()))));
                } else {
                    notifier.fireTestFinished(description);
                }
            }
        }
    }

    public String generateFailedMessage(QUnitAssertion[] assertions) {
        return (new StringBuilder()).append("Failed ").append(qunitPage.getFailedAssertionsSources(assertions)).toString();
    }

    public static RunNotifier getNotifier() {
        return notifier;
    }

    public static void setNotifier(RunNotifier notifier) {
        QUnitTestCase.notifier = notifier;
    }

    public static TestSuite getSuite() {
        return suite;
    }

    public static void setSuite(TestSuite suite) {
        QUnitTestCase.suite = suite;
    }

}
