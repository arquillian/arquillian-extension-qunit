package org.jboss.arquillian.qunit.testng;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.qunit.DefaultModule;
import org.jboss.arquillian.qunit.DefaultTest;
import org.jboss.arquillian.qunit.Module;
import org.jboss.arquillian.qunit.Test;
import org.testng.IMethodInstance;

public class TestSuite {

    @org.junit.Test
    public void test() {
        // having
        List<? extends Test> tests = Arrays.asList(new DefaultTest("test"));
        List<? extends Module> modules = Arrays.asList(new DefaultModule("module", tests));
        
        // when
        TestNGSuite suite = new TestNGSuite(null, modules, null);
        List<IMethodInstance> methodInstances = suite.getMethodInstances();
        
        // then
        assertEquals(1, methodInstances.size());
    }
}
