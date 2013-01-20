package org.jboss.arquillian.qunit.drone;

import java.util.LinkedList;

import org.jboss.arquillian.qunit.junit.TestSuite;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;

public class SuiteRunner extends Suite {

    private Class<?> suiteClass;
    private TestSuite suite;

    public SuiteRunner(Class<?> suiteClass) throws Exception {
        super(suiteClass, new LinkedList<Runner>());

        this.suite = new TestSuite(suiteClass);
    }

    @Override
    public void run(RunNotifier notifier) {
        // Maven doesn't necessarily call getDescription so we need to invoke it in here to ensure
        // all descriptions are cached properly
        getDescription();

        ResultReader.notifier = notifier;
        ResultReader.suite = suite;

        JUnitCore core = new JUnitCore();

        Result result = core.run(ResultReader.class);
    }

    @Override
    public Description getDescription() {
        return suite.getDescription();
    }
}
