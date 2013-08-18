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
package org.jboss.arquillian.qunit.junit.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.arquillian.qunit.api.model.QUnitAssertion;

public final class ReportUtilities {

    private ReportUtilities() {
    }

    public static String generateFailedAssertionMessage(QUnitAssertion[] assertions) {
        if (!ArrayUtils.isEmpty(assertions)) {
            StringBuilder sources = new StringBuilder();
            sources.append("Failed ");
            for (QUnitAssertion assertion : assertions) {
                if (assertion.isFailed() && !StringUtils.isEmpty(assertion.getMessage())) {
                    sources.append(assertion.getMessage()).append(" ");
                }
            }
            return sources.toString();
        }
        return "";
    }
}
