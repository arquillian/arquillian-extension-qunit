package org.jboss.arquillian.qunit.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

public class ReflectOperations {

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
            e.printStackTrace();
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
