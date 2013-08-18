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

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.javascript.JavaScript;
import org.jboss.arquillian.qunit.api.model.QUnitAssertion;
import org.jboss.arquillian.qunit.api.model.QUnitTest;
import org.jboss.arquillian.qunit.junit.model.QUnitAssertionImpl;
import org.jboss.arquillian.qunit.junit.model.QUnitTestImpl;
import org.jboss.arquillian.qunit.junit.utils.StringUtilities;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public class QUnitPage {

    private static final Logger LOGGER = Logger.getLogger(QUnitPage.class.getName());

    @FindBy(jquery = "#qunit-testresult .total")
    private WebElement qunitTestResults;

    private static final String RESULTS_READER_JS = "(function(e){function g(h){if(!h){return null}else{return h.innerText||h.textContent}}function a(k){var j=[];if(k){for(var h=0;h<k.length;h++){var l=g(k[h].querySelector(\".test-source td pre\"));if(l!=null){j.push(l)}}}return j}var f=e.document.querySelectorAll(\"#qunit-tests > li[class~=pass], #qunit-tests > li[class~=fail]\"),c,d;e.arquillianQunitSuiteResults=[];for(var b=0;b<f.length;b++){c=f[b];d=[];d.push(g(c.querySelector(\"span.module-name\")));d.push(g(c.querySelector(\"span.test-name\")));d.push(g(c.querySelector(\"span.runtime\")));d.push(c.getAttribute(\"class\"));d.push(g(c.querySelector(\".failed\")));d.push(g(c.querySelector(\".passed\")));d.push(a(c.querySelectorAll(\".qunit-assert-list > li[class~=fail]\")));e.arquillianQunitSuiteResults.push(d)}})(this);";

    @ArquillianResource
    JavascriptExecutor executor;

    public void waitUntilTestsExecutionIsCompleted() {
        try {
            waitModel().withTimeout(2, TimeUnit.MINUTES).until().element(qunitTestResults).is().present();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error: waitUntilTestsExecutionIsCompleted: Possible stuck suite: ", ex);
        }
    }

    public QUnitTest[] getTests() {

        executor.executeScript(JavaScript.fromString(RESULTS_READER_JS).getSourceCode(), new Object[0]);

        @SuppressWarnings("unchecked")
        List<List<Object>> qunitSuiteResults = (List<List<Object>>) executor.executeScript(
                "return window.arquillianQunitSuiteResults;", new Object[0]);

        if (!CollectionUtils.isEmpty(qunitSuiteResults)) {
            final QUnitTest[] results = new QUnitTestImpl[qunitSuiteResults.size()];
            int qunitTestIndex = 0;
            for (List<Object> qunitTestResultsList : qunitSuiteResults) {
                if (!CollectionUtils.isEmpty(qunitTestResultsList)) {
                    final String moduleName = StringUtilities.trim((String) qunitTestResultsList.get(0));
                    final String testName = StringUtilities.trim((String) qunitTestResultsList.get(1));
                    final String runTime = StringUtilities.trim((String) qunitTestResultsList.get(2));
                    // final String statusClass = StringUtilities.trim(qunitTestResultsList.get(3).toString());
                    final int failedAssertions = Integer.valueOf((String) qunitTestResultsList.get(4));
                    final int passedAssertions = Integer.valueOf((String) qunitTestResultsList.get(5));
                    @SuppressWarnings("unchecked")
                    final List<String> failedAssertionCauseList = (List<String>) qunitTestResultsList.get(6);

                    QUnitAssertion[] qunitFailedAssertions = null;
                    if (!CollectionUtils.isEmpty(failedAssertionCauseList)) {
                        qunitFailedAssertions = new QUnitAssertionImpl[failedAssertionCauseList.size()];
                        int assertionIndex = 0;
                        for (String errorCause : failedAssertionCauseList) {
                            qunitFailedAssertions[assertionIndex++] = (new QUnitAssertionImpl()).setFailed(true).setMessage(
                                    StringUtilities.trim(errorCause));
                        }
                    }

                    final QUnitTest qunitTestResult = (new QUnitTestImpl()).setModuleName(moduleName).setName(testName)
                            .setPassedAssertions(passedAssertions).setFailedAssertions(failedAssertions).setRunTime(runTime)
                            .setFailed(isQunitTestFailed(failedAssertions)).setAssertions(qunitFailedAssertions);

                    results[qunitTestIndex++] = qunitTestResult;
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

    private boolean isQunitTestFailed(int failedAssertions) {
        return failedAssertions > 0;
    }
}
