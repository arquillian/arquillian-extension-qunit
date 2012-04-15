package org.jboss.arquillian.qunit.testng;

import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.arquillian.qunit.Module;
import org.jboss.arquillian.qunit.Test;
import org.jboss.arquillian.qunit.TestInvocator;
import org.jboss.arquillian.qunit.generator.ClassCreator;
import org.mockito.Mockito;
import org.testng.IMethodInstance;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.internal.ConstructorOrMethod;
import org.testng.internal.MethodInstance;
import org.testng.xml.XmlTest;

import com.beust.jcommander.internal.Maps;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;

public class TestNGSuite {

    private TestInvocator invocator;
    private List<? extends Module> modules;
    private Map<? extends Module, Collection<IMethodInstance>> map;

    public TestNGSuite(TestInvocator invocator, List<? extends Module> modules) {
        this.invocator = invocator;
        this.modules = modules;
        this.map = new MapMaker().makeComputingMap(new Function<Module, Collection<IMethodInstance>>() {
            public Collection<IMethodInstance> apply(Module module) {
                return new TestClass(module).getMethodInstances();
            }
        });
    }

    public List<IMethodInstance> getMethodInstances() {
        List<IMethodInstance> instances = Lists.newLinkedList();
        for (Module module : modules) {
            instances.addAll(map.get(module));
        }
        return instances;
    }

    private static class TestClass {

        Module module;
        Class<?> generatedClass;
        Object generatedClassInstance;
        ITestClass testClass;

        public TestClass(Module module) {
            this.module = module;
            Collection<String> testNames = Collections2.transform(module.getTests(), GET_TEST_NAME);
            this.generatedClass = ClassCreator.createClass(module.getName(), testNames.toArray(new String[testNames.size()]));
            this.generatedClassInstance = getGenerateClassInstance();
            this.testClass = createTestClass();
        }

        public Collection<IMethodInstance> getMethodInstances() {
            Collection<ITestNGMethod> methods = Collections2.transform(module.getTests(), GET_METHOD);
            Collection<IMethodInstance> instances = Collections2.transform(methods, GET_METHOD_INSTANCE);
            return instances;
        }

        private ITestClass createTestClass() {
            ITestNGMethod[] NO_METHODS = new ITestNGMethod[] {};
            ITestClass testClass = Mockito.mock(ITestClass.class);
            when(testClass.getName()).thenReturn(module.getName());
            when(testClass.getRealClass()).thenReturn(generatedClass);
            when(testClass.getBeforeTestMethods()).thenReturn(NO_METHODS);
            when(testClass.getAfterTestMethods()).thenReturn(NO_METHODS);
            return testClass;
        }

        private Method getGeneratedMethod(String testName) {
            try {
                return generatedClass.getMethod(testName);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private Object getGenerateClassInstance() {
            try {
                return generatedClass.newInstance();
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
                final String[] EMPTY_STRING_ARRAY = new String[] {};
                final String testName = test.getName();

                ITestNGMethod method = Mockito.mock(ITestNGMethod.class);

                Method generatedMethod = getGeneratedMethod(testName);

                when(method.getMethodName()).thenReturn(testName);
                when(method.getGroups()).thenReturn(EMPTY_STRING_ARRAY);
                when(method.getMethodsDependedUpon()).thenReturn(EMPTY_STRING_ARRAY);
                when(method.getGroupsDependedUpon()).thenReturn(EMPTY_STRING_ARRAY);
                when(method.getTestClass()).thenReturn(testClass);
                when(method.getRealClass()).thenReturn(generatedClass);
                when(method.getMethod()).thenReturn(generatedMethod);
                when(method.getInvocationCount()).thenReturn(1);
                when(method.findMethodParameters(Mockito.any(XmlTest.class))).thenReturn(Maps.<String, String>newHashMap());
                when(method.getConstructorOrMethod()).thenReturn(new ConstructorOrMethod(generatedMethod));
                when(method.toString()).thenReturn(testName + "()");
                when(method.getInstanceHashCodes()).thenReturn(new long[] { 1 });

                return method;
            }
        };

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
