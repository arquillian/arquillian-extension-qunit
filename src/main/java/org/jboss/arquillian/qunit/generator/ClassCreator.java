package org.jboss.arquillian.qunit.generator;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;

public class ClassCreator {

    public static Class<?> createClass(String className, String... methodNames) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass evalClass = pool.makeClass(className);
            for (String methodName : methodNames) {
                evalClass.addMethod(CtNewMethod.make("public void " + methodName + " () {}",
                        evalClass));
            }
            return evalClass.toClass();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
