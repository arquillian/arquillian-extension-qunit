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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.qunit.api.model.TestMethod;
import org.jboss.arquillian.qunit.api.model.TestSuite;
import org.jboss.arquillian.qunit.api.plugin.CodeCoverageQUnitTestSuiteHook;
import org.jboss.arquillian.qunit.api.plugin.CodeCoverageQUnitTestSuitesHook;
import org.jboss.arquillian.qunit.pages.QUnitSuitePageImpl;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    private QUnitSuitePageImpl qunitPage;

    private static TestSuite suite;

    private static RunNotifier notifier;

    private static Map<String, List<String>> expectedTestsBySuiteName = null;

    private static Archive<?> archive = null;

    @Deployment(testable = false)
    public static Archive<?> deployment() throws IOException {
        return QUnitTestCase.getArchive();
    }

    @Test
    public void executeQUnitTestSuites() throws IOException {
        QUnitTestCase.setArchive(null);

        final TestMethod[] qunitTestMethods = suite.getTestMethods();
        if (!ArrayUtils.isEmpty(qunitTestMethods)) {

            List<String> coverageFolders = new ArrayList<String>();
            for (TestMethod testMethod : qunitTestMethods) {
                if (!StringUtils.isEmpty(testMethod.getQUnitTestSuiteFilePath())) {
                    executeQunitTestSuite(testMethod, coverageFolders);
                }
            }
            
            // Aggregate code coverage reports via plugins
            if (coverageFolders.size() > 1) {
                ServiceLoader<CodeCoverageQUnitTestSuitesHook> codeCoverageTestSuitesServiceLoader = ServiceLoader.load(CodeCoverageQUnitTestSuitesHook.class);
                Iterator<CodeCoverageQUnitTestSuitesHook> iterator = codeCoverageTestSuitesServiceLoader.iterator();
                
                while(iterator.hasNext()) {
                    System.out.println("QUnit code coverage aggregation plugin found");
                    CodeCoverageQUnitTestSuitesHook current = iterator.next();
                    current.processTestSuitesResults(suite, coverageFolders);
                }
            }
        }
    }

    private void executeQunitTestSuite(TestMethod testMethod, List<String> coverageFolders) {
        try {
            driver.get((new StringBuilder()).append(contextPath.toExternalForm())
                    .append(testMethod.getQUnitTestSuiteFilePath()).toString());
            LOGGER.log(Level.INFO, (new StringBuilder()).append("Waiting for: ").append(testMethod.getQUnitTestSuiteFilePath())
                    .append(" QUnit Test Suite to finish..").toString());
            // wait until the suite is completed
            qunitPage.waitUntilTestsExecutionIsCompleted();

            // Generate code coverage reports via plugins
            ServiceLoader<CodeCoverageQUnitTestSuiteHook> codeCoverageTestSuiteServiceLoader = ServiceLoader.load(CodeCoverageQUnitTestSuiteHook.class);
            Iterator<CodeCoverageQUnitTestSuiteHook> iterator = codeCoverageTestSuiteServiceLoader.iterator();
            
            while(iterator.hasNext()) {
                System.out.println("QUnit code coverage report plugin found");
                CodeCoverageQUnitTestSuiteHook current = iterator.next();
                current.processTestSuiteResults(suite, qunitPage, testMethod.getQUnitTestSuiteFilePath().split("/"), coverageFolders);
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error: executeQunitTestSuite: ", ex);
        } finally {
            try {
                // create reporter
                QUnitJUnitReporter reporter = new QUnitJUnitReporter(suite, testMethod, notifier, qunitPage.getTests(),
                        expectedTestsBySuiteName);
                // report
                reporter.report();
            } catch (Exception ignore) {
                LOGGER.log(Level.SEVERE, "Error: executeQunitTestSuite: report: ", ignore);
            }
        }
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

    public static Map<String, List<String>> getExpectedTestsBySuiteName() {
        return expectedTestsBySuiteName;
    }

    public static void setExpectedTestsBySuiteName(Map<String, List<String>> expectedTestsBySuiteName) {
        QUnitTestCase.expectedTestsBySuiteName = expectedTestsBySuiteName;
    }

    public static Archive<?> getArchive() {
        return archive;
    }

    public static void setArchive(Archive<?> archive) {
        QUnitTestCase.archive = archive;
    }
}
