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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.arquillian.qunit.utils.MapUtilities;
import org.junit.runner.Description;

public final class DescriptionUtils {

    private DescriptionUtils() {
    }

    public static Map<String, Description> addChildDescriptions(Description suiteDescription, Class<?> clazz,
            Map<String, List<String>> expectedTestsBySuiteName) {
        final Map<String, Description> map = new HashMap<String, Description>();
        if (!MapUtilities.isEmpty(expectedTestsBySuiteName)) {
            Collection<List<String>> collection = expectedTestsBySuiteName.values();
            for (List<String> testList : collection) {
                for (String test : testList) {
                    final String uniqname = NamingUtils.createUniqueTestName(test);
                    final Description desc = Description.createTestDescription(clazz, uniqname);
                    suiteDescription.addChild(desc);
                    map.put(uniqname, desc);
                }
            }
        }
        return map;
    }
}
