package org.jboss.arquillian.qunit.drone;

import static org.jboss.arquillian.graphene.Graphene.element;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.qunit.junit.Packager;
import org.jboss.arquillian.qunit.junit.TestSuite;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.Description;
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

        waitModel().until(element(By.cssSelector("#qunit-testresult .failed")).isPresent());

        String lastModule = null;
        Description moduleDescription = null;

        for (Test test : tests) {


            String module = test.getModule();

            if (moduleChanged(lastModule, module)) {
                if (moduleDescription != null) {
                    notifier.fireTestFinished(moduleDescription);
                }
                moduleDescription = Description.createTestDescription(suite.getJavaClass(), module);
                notifier.fireTestStarted(moduleDescription);
            }

            Description testDescription = Description.createTestDescription(suite.getJavaClass(), test.getName());

            if (moduleDescription != null) {
                moduleDescription.addChild(testDescription);
            } else {
                suite.getDescription().addChild(testDescription);
            }

            notifier.fireTestStarted(testDescription);

            if (test.isFailed()) {
                notifier.fireTestFailure(new Failure(testDescription, new IllegalStateException()));
            } else {
                notifier.fireTestFinished(testDescription);
            }
        }

        if (moduleDescription != null) {
            notifier.fireTestFinished(moduleDescription);
        }
    }

    private boolean moduleChanged(String oldModule, String newModule) {
        return oldModule == null ? (newModule != null) : (!oldModule.equals(newModule));
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
