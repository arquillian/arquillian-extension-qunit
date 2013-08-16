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

import java.util.Comparator;

import org.jboss.arquillian.qunit.api.model.TestMethod;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public final class TestMethodComparator implements Comparator<TestMethod> {

    private TestMethodComparator() {
    }

    private static class SingletonHolder {
        public static final TestMethodComparator INSTANCE = new TestMethodComparator();
    }

    public int compare(TestMethod q1, TestMethod q2) {
        return q1.getSequence() < q2.getSequence() ? -1 : 1;
    }

    public static TestMethodComparator getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
