

import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.qunit.Module;
import org.jboss.arquillian.qunit.testng.TestNGSuite;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class QUnitMethodAddingInterceptor implements IMethodInterceptor, ITestListener {

    private List<Module> modules = new TestModuleReader().readModules(null);
    
    TestNGSuite suite;

    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        
        System.out.println("intercept");

        if (suite == null) {
            suite = new TestNGSuite(null, modules, context.getSuite().getAnnotationFinder());
        }

        return suite.getMethodInstances();
    }

    public void onTestStart(ITestResult result) {
    }

    public void onTestSuccess(ITestResult result) {
        System.out.println("onTestSuccess");
    }

    public void onTestFailure(ITestResult result) {
        System.out.println("onTestFailure");
    }

    public void onTestSkipped(ITestResult result) {
        System.out.println("onTestSkipped");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("onTestFailedButWithinSuccessPercentage");
    }

    public void onStart(ITestContext context) {
    }

    public void onFinish(ITestContext context) {
    }

}
