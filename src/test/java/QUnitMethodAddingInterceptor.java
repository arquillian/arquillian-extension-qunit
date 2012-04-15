import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;

import org.jboss.arquillian.qunit.generator.ClassCreator;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.internal.BaseTestMethod;
import org.testng.internal.ConstructorOrMethod;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlTest;

import com.beust.jcommander.internal.Maps;

public class QUnitMethodAddingInterceptor implements IMethodInterceptor {

    private static final String[] EMPTY_STRING_ARRAY = new String[] {};
    private static final ITestNGMethod[] NO_METHODS = new ITestNGMethod[] {};

    private Class<?> realInstanceClass = null;

    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        System.out.println("intercept");

        if (realInstanceClass == null) {
            realInstanceClass = ClassCreator.createClass("TestClass", "method1");
        }

        final Object instance = mock(realInstanceClass, new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                System.out.println(invocation.getMethod());
                return invocation.callRealMethod();
            }
        });
        final Class<?> instanceClass = realInstanceClass;

        IMethodInstance iMethodInstance = new IMethodInstance() {

            public ITestNGMethod getMethod() {

                ITestNGMethod method = null;

                try {
                    method = Mockito.mock(ITestNGMethod.class);
                    ITestClass clazz = Mockito.mock(ITestClass.class);

                    Method realMethod = instanceClass.getMethod("method1");

                    when(method.getMethodName()).thenReturn("method1");
                    when(method.getGroups()).thenReturn(EMPTY_STRING_ARRAY);
                    when(method.getMethodsDependedUpon()).thenReturn(EMPTY_STRING_ARRAY);
                    when(method.getGroupsDependedUpon()).thenReturn(EMPTY_STRING_ARRAY);
                    when(method.getTestClass()).thenReturn(clazz);
                    when(method.getRealClass()).thenReturn(instanceClass);
                    when(method.getMethod()).thenReturn(realMethod);
                    when(method.getInvocationCount()).thenReturn(1);
                    when(method.findMethodParameters(Mockito.any(XmlTest.class))).thenReturn(Maps.<String, String>newHashMap());
                    when(method.getConstructorOrMethod()).thenReturn(new ConstructorOrMethod(realMethod));
                    when(method.toString()).thenReturn("method1()");
                    when(method.getInstanceHashCodes()).thenReturn(new long[] { 123 });

                    when(clazz.getName()).thenReturn("TestClass");
                    when(clazz.getRealClass()).thenReturn(instanceClass);
                    when(clazz.getBeforeTestMethods()).thenReturn(NO_METHODS);
                    when(clazz.getAfterTestMethods()).thenReturn(NO_METHODS);
                } catch (Exception e) {
                    throw new IllegalArgumentException(e);
                }

                return method;
            }

            public Object[] getInstances() {
                return new Object[] { getInstance() };
            }

            public Object getInstance() {
                return instance;
            }
        };
        return Arrays.asList(iMethodInstance);
    }

    @SuppressWarnings("serial")
    public static class MyBaseTestMethod extends BaseTestMethod {

        public MyBaseTestMethod(Method method, IAnnotationFinder annotationFinder, Object instance) {
            super(method, annotationFinder, instance);
        }

        @Override
        public ITestNGMethod clone() {
            throw new UnsupportedOperationException();
        }

    }

}
