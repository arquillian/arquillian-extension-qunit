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

import java.util.HashMap;
import java.util.Map;

public final class QUnitTestNameCounter {

    private static final Map<String, Integer> QUNIT_TEST_COUNTER_HM = new HashMap<String, Integer>();

    private QUnitTestNameCounter() {
    }

    private static class SingletonHolder {
        public static final QUnitTestNameCounter INSTANCE = new QUnitTestNameCounter();
    }

    public static QUnitTestNameCounter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void add(String name) {
        QUNIT_TEST_COUNTER_HM.put(name, QUNIT_TEST_COUNTER_HM.containsKey(name) ? (QUNIT_TEST_COUNTER_HM.get(name) + 1) : 1);
    }

    public int getCounter(String name) {
        return QUNIT_TEST_COUNTER_HM.containsKey(name) ? QUNIT_TEST_COUNTER_HM.get(name) : 0;
    }

    public void clear() {
        QUNIT_TEST_COUNTER_HM.clear();
    }
}