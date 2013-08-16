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
package org.jboss.arquillian.qunit.pages;

import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.jboss.arquillian.qunit.junit.utils.WebElementUtils.getTextForElement;
import static org.jboss.arquillian.qunit.junit.utils.WebElementUtils.getTrimmedText;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.qunit.api.model.QUnitAssertion;
import org.jboss.arquillian.qunit.api.model.QUnitTest;
import org.jboss.arquillian.qunit.api.pages.QUnitTestPage;
import org.jboss.arquillian.qunit.junit.model.QUnitAssertionImpl;
import org.jboss.arquillian.qunit.junit.model.QUnitTestImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public class QUnitPage implements QUnitTestPage {

    private static final Logger LOGGER = Logger.getLogger(QUnitPage.class.getName());

    @FindBy(jquery = "#qunit-testresult .total")
    private WebElement qunitTestResults;

    @FindBy(jquery = "#qunit-tests > li")
    private List<WebElement> qunitTestsList;

    private static final String MODULE_NAME_CLASS = "module-name";

    private static final String QUNIT_TEST_NAME_CLASS = "test-name";

    private static final String QUNIT_TEST_RUNTIME_CLASS = "runtime";

    private static final String QUNIT_ASSERTIONS_FAILED_CLASS = "failed";

    private static final String QUNIT_ASSERTIONS_PASSED_CLASS = "passed";

    private static final String QUNIT_ASSERTIONS_SELECTOR = ".qunit-assert-list > li";

    private static final String ASSERTIONS_SOURCE_SELECTOR = ".test-source td pre";

    public void waitUntilTestsExecutionIsCompleted() {
        try {
            waitModel().withTimeout(2, TimeUnit.MINUTES).until().element(qunitTestResults).is().present();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error: waitUntilTestsExecutionIsCompleted: Possible stuck suite: ", ex);
        }
    }

    public int getTestsSize() {
        return qunitTestsList.size();
    }

    public QUnitTest[] getTests() {
        if (!CollectionUtils.isEmpty(qunitTestsList)) {
            final QUnitTest[] results = new QUnitTestImpl[qunitTestsList.size()];
            int qunitTestIndex = 0;
            for (WebElement qunitTest : qunitTestsList) {
                try {

                    final String moduleName = getTextForElement(qunitTest.findElements(By.className(MODULE_NAME_CLASS)), 0);

                    final String qunitTestName = getTextForElement(qunitTest.findElements(By.className(QUNIT_TEST_NAME_CLASS)),
                            0);

                    final String runTime = getTextForElement(qunitTest.findElements(By.className(QUNIT_TEST_RUNTIME_CLASS)), 0);

                    final int passed = Integer.valueOf(getTrimmedText(qunitTest.findElement(By
                            .className(QUNIT_ASSERTIONS_PASSED_CLASS))));
                    final int failed = Integer.valueOf(getTrimmedText(qunitTest.findElement(By
                            .className(QUNIT_ASSERTIONS_FAILED_CLASS))));

                    final List<WebElement> assertions = qunitTest.findElements(ByJQuery
                            .jquerySelector(QUNIT_ASSERTIONS_SELECTOR));

                    final QUnitTest qunitTestResult = (new QUnitTestImpl()).setModuleName(moduleName).setName(qunitTestName)
                            .setPassedAssertions(passed).setFailedAssertions(failed).setRunTime(runTime)
                            .setFailed(isQunitTestFailed(failed));

                    if (!CollectionUtils.isEmpty(assertions)) {
                        QUnitAssertion[] qunitAssertions = new QUnitAssertionImpl[assertions.size()];
                        int assertionIndex = 0;
                        for (WebElement assertion : assertions) {
                            final boolean pass = assertion.getAttribute("className").equalsIgnoreCase("pass");
                            final QUnitAssertion assertionDTO = (new QUnitAssertionImpl()).setFailed(!pass).setMessage(
                                    !pass ? getTrimmedText(assertion.findElement(ByJQuery
                                            .jquerySelector(ASSERTIONS_SOURCE_SELECTOR))) : null);
                            qunitAssertions[assertionIndex++] = assertionDTO;
                        }
                        qunitTestResult.setAssertions(qunitAssertions);
                    }

                    results[qunitTestIndex++] = qunitTestResult;
                } catch (Exception ignore) {
                    LOGGER.log(Level.WARNING, "QUnitPage: getTests: Possible stuck suite: Error", ignore);
                }
            }
            return results;
        }
        return null;
    }

    public String getFailedAssertionMessages(QUnitAssertion[] assertions) {
        if (!ArrayUtils.isEmpty(assertions)) {
            StringBuilder sources = new StringBuilder();
            for (QUnitAssertion assertion : assertions) {
                if (assertion.isFailed() && !StringUtils.isEmpty(assertion.getMessage())) {
                    sources.append(assertion.getMessage()).append(" ");
                }
            }
            return sources.toString();
        }
        return "";
    }

    private boolean isQunitTestFailed(int failed) {
        return failed > 0;
    }
}
