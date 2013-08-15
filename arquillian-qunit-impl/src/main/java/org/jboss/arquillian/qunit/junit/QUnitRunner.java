/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.qunit.junit;

import java.util.LinkedList;

import org.jboss.arquillian.qunit.api.model.TestSuite;
import org.jboss.arquillian.qunit.junit.model.TestSuiteImpl;
import org.jboss.arquillian.qunit.junit.test.QUnitTestCase;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public class QUnitRunner extends Suite {

    private TestSuite suite;

    private Description desc;

    public QUnitRunner(Class<?> suiteClass) throws Exception {
        super(suiteClass, new LinkedList<Runner>());
        this.suite = new TestSuiteImpl(suiteClass);
    }

    @Override
    public void run(RunNotifier notifier) {
        JUnitCore core = new JUnitCore();
        QUnitTestCase.notifier = notifier;
        QUnitTestCase.suite = suite;
        core.run(QUnitTestCase.class);
    }

    @Override
    public Description getDescription() {
        if (this.desc == null) {
            this.desc = Description.createSuiteDescription(suite.getSuiteClass().getName(), suite.getSuiteClass()
                    .getAnnotations());
        }
        return this.desc;
    }
}
