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
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.javascript.JavaScript;
import org.jboss.arquillian.qunit.api.model.QUnitAssertion;
import org.jboss.arquillian.qunit.api.model.QUnitTest;
import org.jboss.arquillian.qunit.api.pages.QUnitSuitePage;
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
public class QUnitSuitePageImpl implements QUnitSuitePage {

    private static final Logger LOGGER = Logger.getLogger(QUnitSuitePageImpl.class.getName());

    @FindBy(css = "p#qunit-testresult .total")
    private WebElement qunitTestResults;

    private static final String RESULTS_READER_JS = "(function(m){function g(i){if(!i){return null}else{return i.innerText||i.textContent}}function j(y,x,t){var w=[];var A=y.getElementsByTagName(\"*\");var v;for(var u=0;u<A.length;u++){v=A[u];var z=v.getAttribute(x);if(z&&z===t){w.push(v)}}return w}function h(v,w,B,A){var x=[];var t=v.getElementsByTagName(w);var y;for(var z=0;z<t.length;z++){y=t[z];var u=y.getAttribute(B);if(u&&u===A){x.push(y)}}return x}function s(B){var E=[];if(B){var y,C;for(var z=0;z<B.length;z++){y=B[z],C=y.getAttribute(\"class\");if(C&&C===\"fail\"){var v=y.childNodes;if(v&&v.length>0){var A=false;for(var x=0;x<v.length;x++){var u=v[x];if(u&&u.tagName&&u.tagName.toUpperCase()===\"TABLE\"){A=true;var w=j(u,\"class\",\"test-source\");if(w&&w.length>0){var t=w[0].getElementsByTagName(\"td\");if(t&&t.length>0){var D=g(t[0]);if(D!=null){E.push(D);break}}}}}if(!A){var D=g(y);if(D!=null){E.push(D)}}}else{var D=g(y);if(D!=null){E.push(D)}}}}}return E}var f=m.document.getElementById(\"qunit-tests\"),n=f.childNodes,c,d,r,q,e,l,a,b,k,o;m.arquillianQunitSuiteResults=[];for(var p=0;p<n.length;p++){c=n[p];d=c.getAttribute(\"class\");if(d&&(d===\"pass\"||d===\"fail\")){r=[];q=h(c,\"span\",\"class\",\"module-name\");e=h(c,\"span\",\"class\",\"test-name\");l=h(c,\"span\",\"class\",\"runtime\");a=j(c,\"class\",\"failed\");b=j(c,\"class\",\"passed\");k=h(c,\"ol\",\"class\",\"qunit-assert-list\");if(k===undefined||k.length===0){k=c.getElementsByTagName(\"ol\")}o=k&&k.length>0?k[0].childNodes:null;r.push(g(q&&q.length>0?q[0]:null));r.push(g(e&&e.length>0?e[0]:null));r.push(g(l&&l.length>0?l[0]:null));r.push(g(a&&a.length>0?a[0]:null));r.push(g(b&&b.length>0?b[0]:null));r.push(s(o));m.arquillianQunitSuiteResults.push(r)}}})(this);";

    @ArquillianResource
    private JavascriptExecutor executor;

    public void waitUntilTestsExecutionIsCompleted() {
        try {
            waitModel().withTimeout(2, TimeUnit.MINUTES).until().element(qunitTestResults).is().present();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error: waitUntilTestsExecutionIsCompleted: Possible stuck suite: ", ex);
        }
    }

    public QUnitTest[] getTests() {
        try {
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
                        final int failedAssertions = Integer.valueOf((String) qunitTestResultsList.get(3));
                        final int passedAssertions = Integer.valueOf((String) qunitTestResultsList.get(4));
                        @SuppressWarnings("unchecked")
                        final List<String> failedAssertionCauseList = (List<String>) qunitTestResultsList.get(5);

                        QUnitAssertion[] qunitFailedAssertions = null;
                        if (!CollectionUtils.isEmpty(failedAssertionCauseList)) {
                            qunitFailedAssertions = new QUnitAssertionImpl[failedAssertionCauseList.size()];
                            int assertionIndex = 0;
                            for (String errorCause : failedAssertionCauseList) {
                                qunitFailedAssertions[assertionIndex++] = (new QUnitAssertionImpl()).setFailed(true)
                                        .setMessage(StringUtilities.trim(errorCause != null ? errorCause.toString() : null));
                            }
                        }

                        final QUnitTest qunitTestResult = (new QUnitTestImpl()).setModuleName(moduleName).setName(testName)
                                .setPassedAssertions(passedAssertions).setFailedAssertions(failedAssertions)
                                .setRunTime(runTime).setFailed(isQunitTestFailed(failedAssertions))
                                .setAssertions(qunitFailedAssertions);

                        results[qunitTestIndex++] = qunitTestResult;
                    }
                }
                return results;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error: getTests: Error: ", ex);
        }

        return null;
    }

    private boolean isQunitTestFailed(int failedAssertions) {
        return failedAssertions > 0;
    }
}
