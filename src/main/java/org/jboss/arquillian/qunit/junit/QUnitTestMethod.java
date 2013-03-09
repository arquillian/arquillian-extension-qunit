package org.jboss.arquillian.qunit.junit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class QUnitTestMethod {

    private Method method;

    private int sequence;

    private Annotation[] annotations;

    public QUnitTestMethod(Method m, int s, Annotation[] a) {
        this.method = m;
        this.annotations = a;
        this.sequence = s;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }
}
