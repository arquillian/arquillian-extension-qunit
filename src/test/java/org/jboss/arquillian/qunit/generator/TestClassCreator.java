package org.jboss.arquillian.qunit.generator;
import java.lang.reflect.Method;

import org.jboss.arquillian.qunit.generator.ClassCreator;
import org.junit.Test;

public class TestClassCreator {

    @Test
    public void testClassCreator() throws Exception {

        Class<?> createdClass = ClassCreator.createClass("ClassName", "method1");
        Object instance = createdClass.newInstance();
        Method method = createdClass.getMethod("method1");
        method.invoke(instance);
    }
}
