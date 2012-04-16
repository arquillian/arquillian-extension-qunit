package org.jboss.arquillian.qunit.testng;

import java.util.List;

import org.jboss.arquillian.qunit.Module;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

import com.google.common.collect.Lists;

public class QUnitMethodAddingInterceptor implements IMethodInterceptor, ITestListener {

    private List<Module> modules = new TestModuleReader().readModules(null);

    TestNGSuite suite;

    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {

        System.out.println("intercept");

        if (suite == null) {
            List<Class<? extends ArquillianQUnit>> qunitClasses = Lists.newLinkedList();

            List<XmlTest> xmlTtests = context.getSuite().getXmlSuite().getTests();
            for (XmlTest xmlTest : xmlTtests) {
                List<XmlClass> xmlClasses = xmlTest.getClasses();
                for (XmlClass xmlClass : xmlClasses) {
                    Class<?> clazz = xmlClass.getSupportClass();
                    if (ArquillianQUnit.class.isAssignableFrom(clazz)) {
                        qunitClasses.add((Class<? extends ArquillianQUnit>) clazz);
                    }
                }
            }

            System.out.println(qunitClasses);

            suite = new TestNGSuite(null, modules, qunitClasses.get(0));
        }

        return suite.getMethodInstances();
    }

    public void onTestStart(ITestResult result) {
        beforeTest(result);
    }

    public void onTestSuccess(ITestResult result) {
        afterTest(result);
    }

    public void onTestFailure(ITestResult result) {
        afterTest(result);
    }

    public void onTestSkipped(ITestResult result) {
        afterTest(result);
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        afterTest(result);
    }

    private void beforeTest(ITestResult result) {
        suite.checkAndInvokeBeforeClass(result);
        suite.invokeBeforeTest(result);
    }

    private void afterTest(ITestResult result) {
        suite.invokeAfterTest(result);
        suite.checkAndInvokeAfterClass(result);
    }

    public void onStart(ITestContext context) {
    }

    public void onFinish(ITestContext context) {
    }

}
