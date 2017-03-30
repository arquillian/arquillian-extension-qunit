/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.qunit.junit.notifier;

import java.io.File;
import java.util.LinkedList;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;

/**
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 *
 */
public class RunnerWithCustomNotifier extends Suite {

    private Class<?> suiteClass;
    private Description description;
    private Description suite1Description;
    private Description suite2Description;
    private Description test1Description;
    private Description test2Description;

    public RunnerWithCustomNotifier(Class<?> suiteClass) throws Exception {
        super(suiteClass, new LinkedList<Runner>());
        this.suiteClass = suiteClass;
        this.build();
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.fireTestStarted(this.description);
        notifier.fireTestStarted(this.suite1Description);
        notifier.fireTestStarted(this.test1Description);
        notifier.fireTestFinished(this.test1Description);
        notifier.fireTestFinished(this.suite1Description);
        notifier.fireTestStarted(this.suite2Description);
        notifier.fireTestStarted(this.test2Description);
        notifier.fireTestFinished(this.test2Description);
        notifier.fireTestFinished(this.suite2Description);
        notifier.fireTestFinished(this.description);
    }

    @Override
    public Description getDescription() {
        return description;
    }

    private void build() {
        if (this.suite1Description == null) {
            this.description = Description.createSuiteDescription(this.suiteClass);
            this.suite1Description = Description.createSuiteDescription(new File("pom.xml").getAbsolutePath());
            this.test1Description = Description.createTestDescription(this.suiteClass, "test1");
            this.suite2Description = Description.createSuiteDescription("suite2");
            this.test2Description = Description.createTestDescription(this.suiteClass, "test2");
            this.suite1Description.addChild(this.test1Description);
            this.suite2Description.addChild(this.test2Description);
            this.description.addChild(this.suite1Description);
            this.description.addChild(this.suite2Description);
        }
    }
}
