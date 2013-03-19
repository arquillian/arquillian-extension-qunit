package org.jboss.arquillian.qunit.junit;

import static org.jboss.arquillian.graphene.Graphene.element;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.javascript.JavaScript;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(Arquillian.class)
@RunAsClient
public class ResultReader {

    private Logger LOG = Logger.getLogger(ResultReader.class.getSimpleName());

    public static TestSuite suite;

    public static RunNotifier notifier;

    private static final String EMPTY_MODULE_NAME = "null";

    private DoneFunctionIterator doneFunctionIterator;

    private int testFileIndex = 0;

    private long timeout = 60;

    private TimeUnit unit = TimeUnit.SECONDS;

    @ArquillianResource
    private URL contextPath;

    @ArquillianResource
    JavascriptExecutor executor;

    @Drone
    WebDriver driver;

    @FindBy(className = "total")
    WebElement TESTS_PER_QUNIT_TEST_CASE_COMPLETED;

    @FindBy(jquery = "#qunit-tests > li")
    List<WebElement> TESTS_PER_QUNIT_TEST;

    private Integer totalTests = 0;

    private JavaScript script = JavaScript.fromResource("read-results.js");

    @Deployment(testable = true)
    public static Archive<?> deployment() {
        return Packager.createPackage(suite);
    }

    @org.junit.Test
    public void test() {
        final LinkedHashSet<String> qunitTestsHS = suite.getSortedUniqueQunitTestValues();
        if (!CollectionUtils.isEmpty(qunitTestsHS)) {
            final Iterator<String> qunitTestIter = qunitTestsHS.iterator();
            while (qunitTestIter.hasNext()) {
                final String qunitTestVal = qunitTestIter.next();
                if (!StringUtils.isEmpty(qunitTestVal)) {
                    executeQunitTest(qunitTestVal);
                }
            }
        }
    }

    private void executeQunitTest(String qunitTestValue) {

        try {
            driver.get((new StringBuilder()).append(contextPath.toExternalForm()).append(qunitTestValue).toString());
            // execute the test cases in the given order
            executor.executeScript("QUnit.config.reorder = false;");
            waitModel().withTimeout(timeout, unit).until(element(TESTS_PER_QUNIT_TEST_CASE_COMPLETED).isPresent());
            final TestFile testFile = (new ArrayList<TestFile>(suite.getFiles())).get(testFileIndex++);
            final UniqueName uniqueTestName = new UniqueName();
            doneFunctionIterator = new DoneFunctionIterator(testFile);
            int testIndex = 0;
            updateTotalTestsSize();

            while (!testsPerQunitTestFinished(testIndex, TESTS_PER_QUNIT_TEST.size())) {

                @SuppressWarnings("unchecked")
                final List<List<String>> result = (List<List<String>>) (executor.executeScript(script.getSourceCode(),
                    testIndex));

                for (final List<String> testResult : result) {

                    testIndex++;
                    final String testName = uniqueTestName.getName(testResult.get(1), testResult.get(0));
                    final String moduleName = !StringUtils.isEmpty(testResult.get(1)) ? testResult.get(1) : EMPTY_MODULE_NAME;
                    final boolean failed = testResult.get(2).contains("fail");
                    final TestModule module = testFile.getOrAddModule(moduleName);

                    if (module != null) {
                        final TestFunction function = module.getFunction(testName);
                        if (function != null) {
                            function.markDone();
                            function.setFailed(failed);
                        } else {
                            LOG.warning((new StringBuilder()).append("## Function with name: '").append(testName)
                                .append("' was not found").toString());
                        }
                    } else {
                        LOG.warning((new StringBuilder()).append("## Moddule with name: '").append(moduleName)
                            .append("' was not found").toString());
                    }

                    reportResults();
                }

            }

            reportResults();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, (new StringBuilder()).append(" executeQunitTest: qunitTestValue: '").append(qunitTestValue)
                .append("'").toString(), e);
        }
    }

    private boolean testsPerQunitTestFinished(int testNumber, int totalTestsPerCase) {
        return totalTestsPerCase <= testNumber;
    }

    private void updateTotalTestsSize() {
        totalTests += TESTS_PER_QUNIT_TEST.size();
    }

    private void reportResults() {
        while (doneFunctionIterator.hasNext()) {
            final TestFunction reportFunction = doneFunctionIterator.next();
            notifier.fireTestStarted(reportFunction.getDescription());
            if (reportFunction.isFailed()) {
                notifier.fireTestFailure(new Failure(reportFunction.getDescription(), new Exception("failed")));
            } else {
                notifier.fireTestFinished(reportFunction.getDescription());
            }
        }
    }
}
