package org.jboss.arquillian.qunit.junit;

import java.util.LinkedList;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;

public class QUnitRunner extends Suite {

    private TestSuite suite;

    public QUnitRunner(Class<?> suiteClass) throws Exception {
        super(suiteClass, new LinkedList<Runner>());

        this.suite = SuiteReader.read(suiteClass);
    }

    @Override
    public void run(RunNotifier notifier) {
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
