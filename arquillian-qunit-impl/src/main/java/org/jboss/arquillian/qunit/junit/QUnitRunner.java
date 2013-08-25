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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jboss.arquillian.qunit.api.exceptions.ArquillianQunitException;
import org.jboss.arquillian.qunit.api.model.TestSuite;
import org.jboss.arquillian.qunit.junit.core.DeploymentPackager;
import org.jboss.arquillian.qunit.junit.core.QUnitTestCase;
import org.jboss.arquillian.qunit.junit.core.QUnitTestCaseSimple;
import org.jboss.arquillian.qunit.junit.core.SuiteReader;
import org.jboss.arquillian.qunit.junit.model.TestSuiteImpl;
import org.jboss.arquillian.qunit.junit.utils.DescriptionUtils;
import org.jboss.arquillian.qunit.junit.utils.QUnitTestNameCounter;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Runner;
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

    private TestSuite suite;

    private Archive<?> archive;

    private Map<String, List<String>> expectedTestsBySuiteName;

    private Description suiteDescription;

    public QUnitRunner(Class<?> suiteClass) throws InitializationError, ArquillianQunitException, IOException {
        super(suiteClass, new LinkedList<Runner>());
        this.suite = new TestSuiteImpl(suiteClass).build();
        this.archive = DeploymentPackager.getInstance().createPackage(suite);
        this.expectedTestsBySuiteName = SuiteReader.getInstance().readQUnitTests(archive, suite);
        if (this.suiteDescription == null) {
            this.suiteDescription = Description.createSuiteDescription(this.suite.getSuiteClass().getName());
            this.suite.setTestDescriptions(DescriptionUtils.addChildDescriptions(this.suiteDescription, suiteClass.getName(),
                    this.expectedTestsBySuiteName));
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        JUnitCore core = new JUnitCore();
        executeTests(core, notifier);
    }

    @Override
    public Description getDescription() {
        return this.suiteDescription;
    }

    private void executeTests(JUnitCore core, RunNotifier notifier) {
        QUnitTestNameCounter.getInstance().clear();
        if (this.suite.getDeploymentMethod() != null) {
            QUnitTestCase.setNotifier(notifier);
            QUnitTestCase.setSuite(this.suite);
            QUnitTestCase.setArchive(this.archive);
            QUnitTestCase.setExpectedTestsBySuiteName(this.expectedTestsBySuiteName);
            core.run(QUnitTestCase.class);
        } else {
            QUnitTestCaseSimple.setNotifier(notifier);
            QUnitTestCaseSimple.setSuite(this.suite);
            QUnitTestCaseSimple.setArchive(this.archive);
            QUnitTestCaseSimple.setExpectedTestsBySuiteName(this.expectedTestsBySuiteName);
            core.run(QUnitTestCaseSimple.class);
        }
    }
}
