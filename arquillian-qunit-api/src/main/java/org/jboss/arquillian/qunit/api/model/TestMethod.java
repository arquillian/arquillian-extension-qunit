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

package org.jboss.arquillian.qunit.api.model;

import java.lang.reflect.Method;

/**
 * An abstraction to describe a test method.
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public interface TestMethod {

    /**
     * The method.
     * 
     * @return {@link Method}
     */
    public Method getMethod();

    /**
     * The method's sequence.
     * 
     * @return Sequence
     */
    public int getSequence();

    /**
     * The Qunit Test File which corresponds to this method.
     * 
     * @return QUnit Test File
     */
    public String getQunitTestFile();
}
