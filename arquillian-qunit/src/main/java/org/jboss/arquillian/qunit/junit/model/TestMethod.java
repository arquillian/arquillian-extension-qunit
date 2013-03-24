/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.arquillian.qunit.junit.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.qunit.junit.annotations.QUnitTest;
import org.jboss.arquillian.qunit.junit.exceptions.ArquillianQunitException;
import org.jboss.arquillian.qunit.reflection.ReflectOperations;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public class TestMethod {

    private Method method;

    private int sequence;

    private String qunitTestFile;

    public TestMethod(Method m) throws ArquillianQunitException {
        if (m == null) {
            throw new ArquillianQunitException("Method is missing");
        }
        this.method = m;
    }

    public Method getMethod() {
        return method;
    }

    public int getSequence() {
        return sequence;
    }

    public String getQunitTestFile() {
        return qunitTestFile;
    }

    public TestMethod build() throws ArquillianQunitException {
        final Annotation inSequence = ReflectOperations.getAnnotation(this.method, InSequence.class);
        this.sequence = inSequence != null ? ((InSequence) inSequence).value() : 0;
        final Annotation qunitTest = ReflectOperations.getAnnotation(this.method, QUnitTest.class);
        this.qunitTestFile = qunitTest != null ? ((QUnitTest) qunitTest).value() : null;

        return this;
    }
}
