package org.jboss.arquillian.qunit.testng;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jboss.arquillian.qunit.Module;
import org.jboss.arquillian.qunit.Test;
import org.jboss.arquillian.qunit.TestInvocator;
import org.jboss.arquillian.qunit.generator.ClassCreator;
import org.jboss.arquillian.testng.Arquillian;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.IMethodInstance;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.internal.ConstructorOrMethod;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlTest;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;

public class TestNGSuite {

    private TestInvocator invocator;
    private List<? extends Module> modules;
    private Map<? extends Module, Collection<IMethodInstance>> map;
    private Arquillian arquillian;
    private IAnnotationFinder annotationFinder;

    public TestNGSuite(TestInvocator invocator, List<? extends Module> modules, IAnnotationFinder annotationFinder) {
        this.invocator = invocator;
        this.modules = modules;
        this.annotationFinder = annotationFinder;
        this.map = new MapMaker().makeComputingMap(new Function<Module, Collection<IMethodInstance>>() {
            public Collection<IMethodInstance> apply(Module module) {
                return new TestClass(module).getMethodInstances();
            }
        });
        this.arquillian = new Arquillian() {
        };
    }

    public List<IMethodInstance> getMethodInstances() {
        List<IMethodInstance> instances = Lists.newLinkedList();
        for (Module module : modules) {
            instances.addAll(map.get(module));
        }
        return instances;
    }

    private class TestClass {

        Module module;
        Class<?> generatedInterface;
        Object generatedClassInstance;
        Class<?> generatedClass;
        ITestClass testClass;

        public TestClass(Module module) {
            this.module = module;
            Collection<String> testNames = Collections2.transform(module.getTests(), GET_TEST_NAME);
            this.generatedInterface = ClassCreator.createInterface(module.getName(),
                    testNames.toArray(new String[testNames.size()]));
            this.generatedClassInstance = getGenerateClassInstance();
            this.generatedClass = generatedClassInstance.getClass();
            
            initializeTestClass();
        }

        public Collection<IMethodInstance> getMethodInstances() {
            Collection<ITestNGMethod> methods = Collections2.transform(module.getTests(), GET_METHOD);
            Collection<IMethodInstance> instances = Collections2.transform(methods, GET_METHOD_INSTANCE);
            return instances;
        }

        private void initializeTestClass() {
            ITestNGMethod[] NO_METHODS = new ITestNGMethod[] {};
            ITestClass testClass = mock(ITestClass.class);
            this.testClass = testClass;
            
            when(testClass.getRealClass()).thenReturn(generatedClass);
            when(testClass.getName()).thenReturn(module.getName());
            
            ITestNGMethod[] beforeClassMethods = getBeforeClassMethods();
            ITestNGMethod[] beforeTestMethods = getBeforeTestMethods();
            ITestNGMethod[] afterTestMethods = getAfterTestMethods();
//            ITestNGMethod[] afterClassMethods = getAfterClassMethods();
            
            when(testClass.getBeforeClassMethods()).thenReturn(NO_METHODS);
            when(testClass.getBeforeTestMethods()).thenReturn(NO_METHODS);
            when(testClass.getAfterTestMethods()).thenReturn(NO_METHODS);
            when(testClass.getAfterClassMethods()).thenReturn(NO_METHODS);
        }
        
        private ITestNGMethod[] getBeforeClassMethods() {
            return new ITestNGMethod[] { createMethod("arquillianBeforeClass") };
        }
        
        private ITestNGMethod[] getBeforeTestMethods() {
            return new ITestNGMethod[] { createMethod("arquillianBeforeTest", Method.class) };
        }
        
        private ITestNGMethod[] getAfterTestMethods() {
            return new ITestNGMethod[] { createMethod("arquillianAfterTest", Method.class) };
        }
//        
//        private ITestNGMethod[] getAfterClassMethods() {
//            return new ITestNGMethod[] { createMethod("arquillianAfterClass") };
//        }
        
        

        private Method getGeneratedMethod(String testName, Class<?>... params) {
            try {
                return generatedClass.getMethod(testName, params);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private Object getGenerateClassInstance() {
            try {
                Answer<Object> answer = new Answer<Object>() {
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        if (invocation.getMethod().getDeclaringClass() == generatedInterface) {
                            System.out.println("skipping");
                            return null;
                        }
                        System.out.println(invocation.getMethod());
                        return invocation.callRealMethod();
                    }
                };

                return mock(arquillian.getClass(), withSettings().extraInterfaces(generatedInterface).spiedInstance(arquillian)
                        .defaultAnswer(answer));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        Function<Test, String> GET_TEST_NAME = new Function<Test, String>() {
            public String apply(Test test) {
                return test.getName();
            }
        };

        Function<Test, ITestNGMethod> GET_METHOD = new Function<Test, ITestNGMethod>() {
            public ITestNGMethod apply(Test test) {
                return createMethod(test.getName());
            }
        };

        private ITestNGMethod createMethod(String methodName, Class<?>... params) {
            final String[] EMPTY_STRING_ARRAY = new String[] {};
            Method generatedMethod = getGeneratedMethod(methodName, params);

            ITestNGMethod method = mock(ITestNGMethod.class);
            when(method.getConstructorOrMethod()).thenReturn(new ConstructorOrMethod(generatedMethod));
            when(method.getMethodName()).thenReturn(methodName);
            when(method.getGroups()).thenReturn(EMPTY_STRING_ARRAY);
            when(method.getMethodsDependedUpon()).thenReturn(EMPTY_STRING_ARRAY);
            when(method.getGroupsDependedUpon()).thenReturn(EMPTY_STRING_ARRAY);
            when(method.getTestClass()).thenReturn(testClass);
            when(method.getRealClass()).thenReturn(generatedClass);
            when(method.getMethod()).thenReturn(generatedMethod);
            when(method.getInvocationCount()).thenReturn(1);
            when(method.findMethodParameters(Mockito.any(XmlTest.class))).thenReturn(Maps.<String, String>newHashMap());
            when(method.getConstructorOrMethod()).thenReturn(new ConstructorOrMethod(generatedMethod));
            when(method.toString()).thenReturn(methodName + "()");
            when(method.getInstanceHashCodes()).thenReturn(new long[] { 1 });

            return method;
        }

        Function<ITestNGMethod, IMethodInstance> GET_METHOD_INSTANCE = new Function<ITestNGMethod, IMethodInstance>() {
            public IMethodInstance apply(final ITestNGMethod method) {
                return new IMethodInstance() {

                    public ITestNGMethod getMethod() {
                        return method;
                    }

                    public Object[] getInstances() {
                        return new Object[] { getInstance() };
                    }

                    public Object getInstance() {
                        return generatedClassInstance;
                    }
                };
            }
        };
    }

}
