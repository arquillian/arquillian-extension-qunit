package org.jboss.arquillian.qunit.testng;

import java.net.MalformedURLException;
import java.util.Collection;

import org.junit.Test;

public class TestSuiteReader {

    @Test
    public void test() throws MalformedURLException {
        Collection<TestModule> modules = SuiteReader.read();

        for (TestModule module : modules) {
            System.out.println(module.getName());
            for (TestFunction function : module.getFunctions()) {
                System.out.println("- " + function.getTestNumber() + ": " + function.getName());
            }
        }
    }
}
