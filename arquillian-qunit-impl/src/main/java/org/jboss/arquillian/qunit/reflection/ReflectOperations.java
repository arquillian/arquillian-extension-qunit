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

package org.jboss.arquillian.qunit.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public class ReflectOperations {

    private static final Logger logger = Logger.getLogger(ReflectOperations.class.getSimpleName());

    public static final Method findFirstMethodWithAnnotation(Method[] m, Class<? extends Annotation> c) {
        if (!ArrayUtils.isEmpty(m) && c != null) {
            for (Method method : m) {
                if (m != null && method.isAnnotationPresent(c)) {
                    return method;
                }
            }
        }
        return null;
    }

    public static final Object invokeMethod(Method m, Object caller, Object... args) {
        try {
            return m != null ? m.invoke(caller, args) : null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception", e);
        }
        return null;
    }

    public static final Annotation getAnnotation(Method m, Class<? extends Annotation> annotationClass) {
        return m != null ? m.getAnnotation(annotationClass) : null;
    }

    public static final ArrayList<Annotation> getAnnotations(Method[] m, Class<? extends Annotation> annotationClass) {
        if (!ArrayUtils.isEmpty(m)) {
            final ArrayList<Annotation> al = new ArrayList<Annotation>();
            for (Method method : m) {
                final Annotation annotation = getAnnotation(method, annotationClass);
                if (annotation != null) {
                    al.add(annotation);
                }
            }
            return al;
        }
        return null;
    }

}
