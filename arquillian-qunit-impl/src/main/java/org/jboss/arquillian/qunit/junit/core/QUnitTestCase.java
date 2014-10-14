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
import java.util.List;
import java.util.Map;
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

            for (TestMethod testMethod : qunitTestMethods) {
                if (!StringUtils.isEmpty(testMethod.getQUnitTestSuiteFilePath())) {
                    executeQunitTestSuite(testMethod);
        /** ===code coverage
            List<String> coverageFolders = new ArrayList<String>();
            for (TestMethod testMethod : qunitTestMethods) {
                if (!StringUtils.isEmpty(testMethod.getQUnitTestSuiteFilePath())) {
                    executeQunitTestSuite(testMethod, coverageFolders);
                }
            }

            // aggregate results and create lcov and cobertura xml
            if (coverageFolders.size() > 1) {
                try {
                    CommandLine cmdLine = new CommandLine("java");
                    cmdLine.addArgument("-cp");
                    cmdLine.addArgument("src/test/resources/jscover/binaries/JSCover-all.jar");
                    cmdLine.addArgument("jscover.report.Main");
                    cmdLine.addArgument("--merge");
                    String[] projectNamePaths = suite.getQUnitResourcesPath().split("/");
                    String projectName = projectNamePaths[projectNamePaths.length - 1];
                    for (String coverageFolder : coverageFolders) {
                        cmdLine.addArgument(coverageFolder);
                    }
                    String aggregationFolder = "target/cc/" + projectName + "/aggregation";
                    cmdLine.addArgument(aggregationFolder);
                    Executor nexecutor = new DefaultExecutor();
                    nexecutor.execute(cmdLine);

                    FileUtils.copyDirectory(new File("src/test/resources/jscover/html-reporter"), new File(aggregationFolder));
                    
                    // create lcov && cobertura xml
                    final String originalSrc = aggregationFolder + "/" + "original-src";
                    cmdLine = new CommandLine("java");
                    cmdLine.addArgument("-cp");
                    cmdLine.addArgument("src/test/resources/jscover/binaries/JSCover-all.jar");
                    cmdLine.addArgument("jscover.report.Main");
                    cmdLine.addArgument("--format=COBERTURAXML");
                    cmdLine.addArgument(aggregationFolder);
                    cmdLine.addArgument(originalSrc);
                    nexecutor = new DefaultExecutor();
                    nexecutor.execute(cmdLine);
                    
                    cmdLine = new CommandLine("java");
                    cmdLine.addArgument("-cp");
                    cmdLine.addArgument("src/test/resources/jscover/binaries/JSCover-all.jar");
                    cmdLine.addArgument("jscover.report.Main");
                    cmdLine.addArgument("--format=LCOV");
                    cmdLine.addArgument(aggregationFolder);
                    cmdLine.addArgument(originalSrc);
                    nexecutor = new DefaultExecutor();
                    nexecutor.execute(cmdLine);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error: coverage aggregation failure: report: ", ex);
                */
                }
            }
        }
    }

    private void executeQunitTestSuite(TestMethod testMethod) {
    /** ===code coverage
    private void executeQunitTestSuite(TestMethod testMethod, List<String> coverageFolders) {
    */
        try {
            driver.get((new StringBuilder()).append(contextPath.toExternalForm())
                    .append(testMethod.getQUnitTestSuiteFilePath()).toString());
            LOGGER.log(Level.INFO, (new StringBuilder()).append("Waiting for: ").append(testMethod.getQUnitTestSuiteFilePath())
                    .append(" QUnit Test Suite to finish..").toString());
            // wait until the suite is completed
            qunitPage.waitUntilTestsExecutionIsCompleted();

            /** ===code coverage

            Object o = qunitPage.executeScript("return jscoverage_serializeCoverageToJSON();", null);

            String[] paths = testMethod.getQUnitTestSuiteFilePath().split("/");
            String fileName = paths[paths.length - 2] + "_"
                    + paths[paths.length - 1].substring(0, paths[paths.length - 1].indexOf("."));
            String[] projectNamePaths = suite.getQUnitResourcesPath().split("/");
            String projectName = projectNamePaths[projectNamePaths.length - 1];
            String destination = "target/cc/" + projectName + "/" + fileName;
            File destFolder = new File(destination);
            destFolder.mkdirs();

            File destFile = new File(destFolder.getAbsolutePath() + "/jscoverage.json");
            destFile.createNewFile();
            PrintWriter writer = new PrintWriter(destFile, "UTF-8");
            writer.println(o.toString());
            writer.close();

            coverageFolders.add(destination);

            // copy src
            FileUtils.copyDirectoryToDirectory(new File(suite.getQUnitResourcesPath() + "/original-src"), destFolder);

            // copy html reporter
            FileUtils.copyDirectory(new File("src/test/resources/jscover/html-reporter"), destFolder);

            */
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
