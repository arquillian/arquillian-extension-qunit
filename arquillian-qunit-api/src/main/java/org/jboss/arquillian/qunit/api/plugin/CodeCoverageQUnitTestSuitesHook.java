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
package org.jboss.arquillian.qunit.api.plugin;

import java.util.List;

import org.jboss.arquillian.qunit.api.model.TestSuite;

/**
 * Service provider interface (SPI) for aggregation of code coverage reports
 *
 * @author pstribny@redhat.com
 */
public interface CodeCoverageQUnitTestSuitesHook {

    /**
     * This method gets called after all test suites have been executed
     *
     * @param test suite
     * @param affected folders
     */
    public void processTestSuitesResults(TestSuite suite, List<String> affectedFolders);
}
