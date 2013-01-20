package org.jboss.arquillian.qunit.notifier;

import java.io.File;
import java.util.LinkedList;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;

public class RunnerWithCustomNofitier extends Suite  {

    private Class<?> suiteClass;

    private Description description;

    private Description suite1Description;
    private Description suite2Description;
    private Description test1Description;
    private Description test2Description;

    public RunnerWithCustomNofitier(Class<?> suiteClass) throws Exception {
        super(suiteClass, new LinkedList<Runner>());
        this.suiteClass = suiteClass;
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.fireTestStarted(suite1Description);
        notifier.fireTestStarted(test1Description);
        notifier.fireTestFinished(test1Description);
        notifier.fireTestFinished(suite1Description);

        notifier.fireTestStarted(suite2Description);
        notifier.fireTestStarted(test2Description);
        notifier.fireTestFinished(test2Description);
        notifier.fireTestFinished(suite2Description);
    }

    @Override
    public Description getDescription() {
        if (suite1Description == null) {
            description = Description.createSuiteDescription(suiteClass);

            suite1Description = Description.createSuiteDescription("" + new File("./pom.xml").getAbsolutePath());
            test1Description = Description.createTestDescription(suiteClass, "test1");

            suite2Description = Description.createSuiteDescription("suite2");
            test2Description = Description.createTestDescription(suiteClass, "test2");

            suite1Description.addChild(test1Description);
            suite2Description.addChild(test2Description);

            description.addChild(suite1Description);
            //description.addChild(suite2Description);
            suite1Description.addChild(suite2Description);

        }
        return description;
    }
}
