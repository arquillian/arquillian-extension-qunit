

import java.util.List;

import org.jboss.arquillian.qunit.Module;
import org.jboss.arquillian.qunit.testng.TestNGSuite;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

public class QUnitMethodAddingInterceptor implements IMethodInterceptor {

    private List<Module> modules = new TestModuleReader().readModules(null);
    
    TestNGSuite suite;

    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        System.out.println("intercept");

        if (suite == null) {
            suite = new TestNGSuite(null, modules);
        }

        return suite.getMethodInstances();
    }

}
