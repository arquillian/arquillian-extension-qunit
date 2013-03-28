/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

import java.lang.reflect.Method;

import org.jboss.arquillian.qunit.api.model.DeploymentMethod;
import org.jboss.arquillian.qunit.reflection.ReflectOperations;
import org.jboss.shrinkwrap.api.Archive;

/**
 *
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 *
 */
public class DeploymentMethodImpl implements DeploymentMethod {

    private Method method;

    public DeploymentMethodImpl(Method m) {
        this.method = m;
    }

    public Method getMethod() {
        return method;
    }

    public Archive<?> getArchive() {
        return this.getMethod() != null ? (Archive<?>) ReflectOperations.invokeMethod(this.getMethod(), this.getMethod()
                .getDeclaringClass()) : null;
    }
}
