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
package org.jboss.arquillian.qunit.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 
 * @author Lukas Fryc
 * @author Tolis Emmanouilidis
 * 
 */
public final class ReflectOperations {

    private ReflectOperations() {
    }

    private static final Logger LOGGER = Logger.getLogger(ReflectOperations.class.getName());

    public static Method findFirstMethodWithAnnotation(Method[] m, Class<? extends Annotation> c) {
        if (!ArrayUtils.isEmpty(m) && c != null) {
            for (Method method : m) {
                if (m != null && method.isAnnotationPresent(c)) {
                    return method;
                }
            }
        }
        return null;
    }

    public static Object invokeMethod(Method m, Object caller, Object... args) {
        try {
            return m != null ? m.invoke(caller, args) : null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "invokeMethod Error", e);
        }
        return null;
    }

    public static Annotation getAnnotation(Method m, Class<? extends Annotation> annotationClass) {
        return m != null ? m.getAnnotation(annotationClass) : null;
    }

    public static List<Annotation> getAnnotations(Method[] m, Class<? extends Annotation> annotationClass) {
        if (!ArrayUtils.isEmpty(m)) {
            final List<Annotation> al = new ArrayList<Annotation>();
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