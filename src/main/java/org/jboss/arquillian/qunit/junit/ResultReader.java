package org.jboss.arquillian.qunit.junit;

import static org.jboss.arquillian.graphene.Graphene.element;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@RunWith(Arquillian.class)
@RunAsClient
public class ResultReader {

    private Logger log = Logger.getLogger(ResultReader.class.getSimpleName());

    public static TestSuite suite;
    public static RunNotifier notifier;

    @Deployment
    public static WebArchive deployment() {
        return Packager.scan(false);
    }

    @ArquillianResource
    private URL contextPath;

    @Drone
    WebDriver driver;

    @FindBy(css = "#qunit-tests > li")
    private List<Test> tests;

    @org.junit.Test
    public void test() throws IOException {

        driver.get(contextPath.toExternalForm() + "test/index.html");

        final TestFile testFile = suite.getFiles().iterator().next();

        waitModel().until(element(By.cssSelector("#qunit-testresult .failed")).isPresent());

        UniqueName uniqueTestName = new UniqueName();

        DoneFunctionIterator doneFunctionIterator = new DoneFunctionIterator(testFile);

        for (Test test : tests) {

            String testName = test.getName();
            String moduleName = test.getModule();
            if (moduleName == null) {
                moduleName = "null";
            }
            testName = uniqueTestName.getName(moduleName, testName);

            TestModule module = testFile.getOrAddModule(moduleName);

            if (module != null) {
                TestFunction function = module.getFunction(testName);
                if (function != null) {
                    function.markDone();
                    function.setFailed(test.isFailed());
                } else {
                    // TODO
                    System.err.println("function with name " + testName + " not found");
                }
            } else {
                // TODO
                System.err.println("module with name " + moduleName + " not found");
            }

            while (doneFunctionIterator.hasNext()) {

                TestFunction reportFunction = doneFunctionIterator.next();

                notifier.fireTestStarted(reportFunction.getDescription());

                if (test.isFailed()) {
                    notifier.fireTestFailure(new Failure(reportFunction.getDescription(), new Exception("failed")));
                } else {
                    notifier.fireTestFinished(reportFunction.getDescription());
                }

            }

        }
    }

    private class Test {

        @Root
        private WebElement root;

        @FindBy(css = "span.test-name")
        private WebElement nameElement;

        @FindBy(css = "span.module-name")
        private WebElement moduleElement;

        public String getName() {
            return nameElement.getText();
        }

        public String getModule() {
            try {
                return moduleElement.getText();
            } catch (NoSuchElementException e) {
                return null;
            }
        }

        public boolean isFailed() {
            return root.getAttribute("class").equals("fail");
        }
    }
}
