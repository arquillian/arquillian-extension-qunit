package org.jboss.arquillian.qunit.testng;

import static org.jboss.arquillian.graphene.Graphene.element;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.testng.Assert.fail;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Predicate;

import javassist.ClassPool;

public class ClassGenerator {

    public Class<?>[] getClasses() {

        try {
            ClassPool classPool = ClassPool.getDefault();

            TestPackager.scan2(true);
            Collection<TestModule> modules = SuiteReader.read();
            TestPackager.scan2(false);

            List<Class<?>> classes = new LinkedList<Class<?>>();

            final CallbackHandler callbackHandler = new CallbackHandler();
            final ExecutionContext context = new ExecutionContext();

            for (final TestModule module : modules) {

                TestCaseGenerator generator = new TestCaseGenerator(module.getName(), classPool, callbackHandler);

                for (final TestFunction function : module.getFunctions()) {

                    generator.addTestMethod(function.getName(), new TestByDrone() {
                        @Override
                        public void call() {
                            delegate(module, function, context);
                        }
                    });
                }

                Class<?> clazz = generator.toClass();
                classes.add(clazz);
            }

            return classes.toArray(new Class<?>[classes.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

    }

    private abstract static class TestByDrone implements Callback {

        public abstract void call();

        public void delegate(final TestModule module, final TestFunction function, final ExecutionContext context) {
            System.out.println(module.getName() + " - " + function.getName());

            if (!context.finished) {
                WebDriver browser = GrapheneContext.getProxy();

                File file = new File("target/qunit-temp/test.war/test/index.html");

                try {
                    browser.get(file.toURL().toExternalForm());
                } catch (MalformedURLException e) {
                    throw new IllegalStateException(e);
                }

                waitModel().until(element(By.cssSelector("#qunit-testresult .failed")).isPresent());

//                List<WebElement> passedTests = browser.findElements(By.cssSelector("#qunit-tests > li.pass"));
//                for (WebElement test : passedTests) {
//                    int testNumber = Integer.parseInt(test.findElement(By.tagName("a")).getAttribute("href").split("=")[1]);
//                    TestResult result = new TestResult(0, null);
//                    context.results.put(testNumber, result);
//                }
                
                List<Long> passes = (List<Long>) ((JavascriptExecutor) browser).executeScript("var passes = document.querySelectorAll('#qunit-tests > li.pass a'), array = []; for (var i = 0; i < passes.length; i++) { var href = passes[i].href; array.push(parseInt(href.substring(href.indexOf('=') + 1))); }; return array");
                
                for (Long pass : passes) {
                    context.results.put(pass, new TestResult(0, null));
                }
                
//                List<WebElement> failedTests = browser.findElements(By.cssSelector("#qunit-tests > li.fail"));
//                for (WebElement test : failedTests) {
//                    int testNumber = Integer.parseInt(test.findElement(By.tagName("a")).getAttribute("href").split("=")[1]);
//                    int failed = Integer.parseInt(test.findElement(By.cssSelector(".failed")).getText());
//                    String failure = null;
//                    if (failed > 0) {
//                        failure = test.findElement(By.cssSelector(".qunit-assert-list .fail")).getText();
//                    }
//
//                    TestResult result = new TestResult(failed, failure);
//
//                    context.results.put(testNumber, result);
//                }
                
                List<Long> failsList = (List<Long>) ((JavascriptExecutor) browser).executeScript("var fails = document.querySelectorAll('#qunit-tests > li.fail a'), array = []; for (var i = 0; i < fails.length; i++) { var href = fails[i].href; array.push(parseInt(href.substring(href.indexOf('=') + 1))); }; return array");
                List<String> failuresList = (List<String>) ((JavascriptExecutor) browser).executeScript("var fails = document.querySelectorAll('#qunit-tests > li.fail .qunit-assert-list .fail:first-child'), array = []; for (var i = 0; i < fails.length; i++) { array.push(fails[i].innerText); }; return array");
                
                Long[] fails = failsList.toArray(new Long[failsList.size()]);
                String[] failures = failuresList.toArray(new String[failuresList.size()]);
                
                for (int i = 0; i < fails.length; i++) {
                    context.results.put(fails[i], new TestResult(1, failures[i]));
                }
                
                context.finished = true;
            }

            TestResult result = context.results.get(Long.valueOf(function.getTestNumber()));

            if (result == null) {
                new IllegalStateException("The test was not ran");
            }

            if (result.failed > 0) {
                fail(result.failure);
            }
        }
    }

    private static class ExecutionContext {
        private Map<Long, TestResult> results = new HashMap<Long, TestResult>();
        private boolean finished = false;
    }

    private static class TestResult {

        private int failed;
        private String failure;

        public TestResult(int failed, String failure) {
            this.failed = failed;
            this.failure = failure;
        }
    }

}
