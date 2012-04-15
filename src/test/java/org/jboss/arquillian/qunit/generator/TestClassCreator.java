package org.jboss.arquillian.qunit.generator;
import java.lang.reflect.Method;

import org.jboss.arquillian.qunit.generator.ClassCreator;
import org.junit.Test;
import org.mockito.Mockito;

public class TestClassCreator {

    @Test
    public void testClassCreator() throws Exception {

        Class<?> createdClass = ClassCreator.createClass("ClassName", "method1");
        Object instance = createdClass.newInstance();
        Method method = createdClass.getMethod("method1");
        method.invoke(instance);
    }
    
    @Test
    public void testInterfaceCreator() throws Exception {

        Class<?> createdClass = ClassCreator.createInterface("InterfaceName", "method1");
        Object instance = Mockito.mock(createdClass);
        Method method = createdClass.getMethod("method1");
        method.invoke(instance);
    }
}
