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
package org.jboss.arquillian.qunit.junit;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jboss.arquillian.qunit.api.model.TestSuite;
import org.jboss.arquillian.qunit.junit.core.DeploymentPackager;
import org.jboss.arquillian.qunit.junit.core.QUnitTestCase;
import org.jboss.arquillian.qunit.junit.core.QUnitTestCaseSimple;
import org.jboss.arquillian.qunit.junit.core.SuiteReader;
import org.jboss.arquillian.qunit.junit.model.TestSuiteImpl;
import org.jboss.arquillian.qunit.junit.utils.DescriptionUtils;
import org.jboss.arquillian.qunit.junit.utils.QUnitTestNameCounter;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

/**
 *
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 *
 */
public class QUnitRunner extends Suite {

    // this is used to ignore method that encapsulates test QUnit test execution for method results
    private static final RunNotifier IGNORING_RUN_NOTIFIER = new RunNotifier();

    private TestSuite suite;

    private Archive<?> archive;

    private Map<String, List<String>> expectedTestsBySuiteName;

    private Description suiteDescription;

    public QUnitRunner(Class<?> suiteClass) throws InitializationError, IOException {
        super(suiteClass, new LinkedList<Runner>());
        this.suite = new TestSuiteImpl(suiteClass).build();
        this.archive = DeploymentPackager.getInstance().createPackage(suite);
        this.expectedTestsBySuiteName = SuiteReader.getInstance().readQUnitTests(archive, suite);
        if (this.suiteDescription == null) {
            this.suiteDescription = Description.createSuiteDescription(this.suite.getSuiteClass().getName());
            this.suite.setTestDescriptions(DescriptionUtils.addChildDescriptions(this.suiteDescription, suiteClass,
                this.expectedTestsBySuiteName));
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            executeTests(notifier);
        } catch (Throwable e) {
            // FIXME ArquillianQunit exception is useless here, because we can' throw it
            throw new RuntimeException(e);
        }
    }

    @Override
    public Description getDescription() {
        return this.suiteDescription;
    }

    private void executeTests(RunNotifier notifier) throws Throwable {
        QUnitTestNameCounter.getInstance().clear();

        IGNORING_RUN_NOTIFIER.addListener(new FailListener(notifier, this.suiteDescription));

        if (this.suite.getDeploymentMethod() != null) {
            QUnitTestCase.setNotifier(notifier);
            QUnitTestCase.setSuite(this.suite);
            QUnitTestCase.setArchive(this.archive);
            QUnitTestCase.setExpectedTestsBySuiteName(this.expectedTestsBySuiteName);
            new AllDefaultPossibilitiesBuilder(true).runnerForClass(QUnitTestCase.class).run(IGNORING_RUN_NOTIFIER);
        } else {
            QUnitTestCaseSimple.setNotifier(notifier);
            QUnitTestCaseSimple.setSuite(this.suite);
            QUnitTestCaseSimple.setArchive(this.archive);
            QUnitTestCaseSimple.setExpectedTestsBySuiteName(this.expectedTestsBySuiteName);
            new AllDefaultPossibilitiesBuilder(true).runnerForClass(QUnitTestCaseSimple.class).run(IGNORING_RUN_NOTIFIER);
        }
    }

    public class FailListener extends RunListener {

        private RunNotifier qunitRunNotifier;
        private Description suiteDescription;

        public FailListener(RunNotifier qunitRunNotifier, Description suiteDescription) {
            super();
            this.qunitRunNotifier = qunitRunNotifier;
            this.suiteDescription = suiteDescription;
        }

        @Override
        public void testFailure(Failure failure) throws Exception {
            this.qunitRunNotifier.fireTestFailure(new Failure(this.suiteDescription, failure.getException()));
        }
    }
}
