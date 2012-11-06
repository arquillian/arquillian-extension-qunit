package org.jboss.arquillian.qunit.testng;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javassist.ClassPool;

public class ClassGenerator {

    public Class<?>[] getClasses() {

        try {
            ClassPool classPool = ClassPool.getDefault();
            
            Collection<TestModule> modules = SuiteReader.read();
            List<Class<?>> classes = new LinkedList<Class<?>>();
            
            CallbackHandler callbackHandler = new CallbackHandler();
            
            for (final TestModule module : modules) {

                TestCaseGenerator generator = new TestCaseGenerator(module.getName(), classPool, callbackHandler);
                      
                for (final TestFunction function : module.getFunctions()) {
                    
                    generator.addTestMethod(function.getName(), new Callback() {
                        public void call() {
                            System.out.println(module.getName() + " - " + function.getName());
                        }
                    });
                }
    
                Class<?> clazz = generator.toClass();
                classes.add(clazz);
            }

            return classes.toArray(new Class<?>[classes.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

    }

}
