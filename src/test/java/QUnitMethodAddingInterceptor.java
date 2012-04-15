import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

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

    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        
        final Object instance = new QUnitRunner();
        
        IMethodInstance iMethodInstance = new IMethodInstance() {

            public ITestNGMethod getMethod() {
                
                ITestNGMethod method = null;
                
                try {
                    // IAnnotationFinder annotationFinder = Mockito.mock(IAnnotationFinder.class, new Answer<Object>() {
                    // public Object answer(InvocationOnMock invocation) throws Throwable {
                    // System.out.println("IAnnotationFinder: " + invocation.getMethod());
                    // return null;
                    // }
                    // });
                    //
                    // Object instance = new TestNG();
                    // Method realMethod = TestNG.class.getMethod("method");
                    // new MyBaseTestMethod(realMethod, annotationFinder, instance);

//                    method = Mockito.mock(ITestNGMethod.class, new Answer<Object>() {
//                        public Object answer(InvocationOnMock invocation) throws Throwable {
//                            System.out.println("ITestNGMethod: " + invocation.getMethod());
//                            return null;
//                        }
//                    });
//
//                    ITestClass clazz = Mockito.mock(ITestClass.class, new Answer<Object>() {
//                        public Object answer(InvocationOnMock invocation) throws Throwable {
//                            System.out.println("ITestClass: " + invocation.getMethod());
//                            return null;
//                        }
//                    });
                    method = Mockito.mock(ITestNGMethod.class);
                    ITestClass clazz = Mockito.mock(ITestClass.class);
                    
                    Method realMethod = QUnitRunner.class.getMethod("method1");

                    when(method.getMethodName()).thenReturn("methodName");
                    when(method.getGroups()).thenReturn(EMPTY_STRING_ARRAY);
                    when(method.getMethodsDependedUpon()).thenReturn(EMPTY_STRING_ARRAY);
                    when(method.getGroupsDependedUpon()).thenReturn(EMPTY_STRING_ARRAY);
                    when(method.getTestClass()).thenReturn(clazz);
                    when(method.getRealClass()).thenReturn(QUnitRunner.class);
                    when(method.getMethod()).thenReturn(realMethod);
                    when(method.getInvocationCount()).thenReturn(1);
                    when(method.findMethodParameters(Mockito.any(XmlTest.class))).thenReturn(Maps.<String, String>newHashMap());
                    when(method.getConstructorOrMethod()).thenReturn(new ConstructorOrMethod(realMethod));
                    when(method.toString()).thenReturn("methodName()");
                    when(method.getInstanceHashCodes()).thenReturn(new long[] { 123 });

                    when(clazz.getName()).thenReturn("className");
                    when(clazz.getRealClass()).thenReturn(QUnitRunner.class);
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
